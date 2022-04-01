/**
MIT License

Copyright (c) 2021 Surati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */  
package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.ThirdParties;
import io.surati.gap.payment.base.api.ThirdParty;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * List of third parties in Database.
 * 
 * @since 3.0
 */
public final class DbThirdParties implements ThirdParties {
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbThirdParties(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public ThirdParty get(String code) {
		
		try(
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM pay_third_party WHERE code=?");
		){
			pstmt.setString(1, code);
		
			try(final ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					Long id = rs.getLong(1);
					return new DbThirdParty(source, id);
				} else {
					throw new IllegalArgumentException(String.format("Le Tiers avec le code %s introuvable !", code));
				}			
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public ThirdParty get(Long id) {
		try(
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM pay_third_party WHERE id=?");
		){
			pstmt.setLong(1, id);
		
			try(final ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					return new DbThirdParty(source, id);
				} else {
					throw new IllegalArgumentException(String.format("ThirdParty with ID %s not found !", id));
				}			
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public ThirdParty add(String code, String name, String abbreviated) {
		
		if(name == null || name.trim().isEmpty()) 
			throw new IllegalArgumentException("Le nom du Tiers ne peut être vide !");
		
		if(code == null || code.trim().isEmpty())
			throw new IllegalArgumentException("Le code du Tiers ne peut être vide !");
		
		if(abbreviated == null || abbreviated.trim().isEmpty())
			throw new IllegalArgumentException("L'abrégé du Tiers ne peut être vide ! !");
		
		if(has(code))
			throw new IllegalArgumentException(String.format("Le code %s est déjà utilisé !", code));
		
		try(
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("INSERT INTO ad_person (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				final PreparedStatement pstmt1 = connection.prepareStatement("INSERT INTO pay_third_party (id, code, abbreviated, payment_deadline) VALUES (?, ?, ?, 0)")
		){
			
			pstmt.setString(1, name);
			
			pstmt.executeUpdate();
			
			final Long id;
			try (final ResultSet rs = pstmt.getGeneratedKeys()) {
				rs.next();
				id = rs.getLong(1);
			}
						
			
			pstmt1.setLong(1, id);
			pstmt1.setString(2, code);
			pstmt1.setString(3, abbreviated);
			pstmt1.executeUpdate();
			
            final ThirdParty tp = new DbThirdParty(source, id);
			tp.paymentCondition().add(PaymentMeanType.CHEQUE);
			return tp;
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void remove(final Long id) {
		try {
			final boolean used = new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "SELECT COUNT(*) FROM pay_third_party",
		                    "WHERE id=? AND (id IN (SELECT holder_id FROM pay_bank_account)",
		                    "OR id IN (SELECT beneficiary_id FROM pay_payment_order)",
		                    "OR id IN (SELECT third_party_id FROM pay_payment_order_group)",
		                    "OR id IN (SELECT beneficiary_id FROM pay_bank_note)",
		                    "OR id IN (SELECT issuer_id FROM pay_reference_document))"
		                ).toString()
		            )
		            .set(id)
		            .select(new SingleOutcome<>(Long.class)) > 0;
		        if(used) {
		        	throw new IllegalArgumentException("Le tiers ne peut pas être supprimé (déjà utilisé) !");
		        }
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("DELETE FROM pay_third_party WHERE id=?");
			final PreparedStatement pstmt1 = connection.prepareStatement("DELETE FROM ad_person WHERE id=?");
		) {
			pstmt.setLong(1, id);
			pstmt1.setLong(1, id);
			pstmt.executeUpdate();
			pstmt1.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public Iterable<ThirdParty> iterate() {
		
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM pay_third_party order by abbreviated ASC")
		){
			final Collection<ThirdParty> items = new ArrayList<>();
			
			try(final ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					items.add(new DbThirdParty(source, rs.getLong(1)));
				}
			}
			
			return items;
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public boolean has(String code) {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) as nb FROM pay_third_party WHERE code=?")
		){
			pstmt.setString(1, code);
		
			try(final ResultSet rs = pstmt.executeQuery()){
				rs.next();
				Long nb = rs.getLong(1);
				return nb > 0;
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql("SELECT COUNT(*) FROM pay_third_party")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
