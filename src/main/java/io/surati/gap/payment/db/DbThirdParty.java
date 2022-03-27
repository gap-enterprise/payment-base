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
package io.surati.gap.payment.db;

import com.google.common.base.Objects;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.db.DbAbstractPerson;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.PaymentCondition;
import io.surati.gap.payment.api.ThirdParty;
import io.surati.gap.payment.api.ThirdPartyFamily;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Third party in Database.
 * 
 * @since 3.0
 */
public final class DbThirdParty extends DbAbstractPerson implements ThirdParty {
	
	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbThirdParty(final DataSource source, final Long id) {
		super(source, id);
	}

	/**
	 * Checks if a ThirdParty with code and not id exists
	 * @param code
	 * @return boolean exits
	 */
	private boolean codeIsUsed(String code) {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) as nb FROM pay_third_party WHERE code=? AND id <>?")
			){
				pstmt.setString(1, code);
				pstmt.setLong(2, id);
			
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
	public String code() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT code FROM pay_third_party WHERE id=?")
		){
			pstmt.setLong(1, id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getString(1);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public String abbreviated() {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("SELECT abbreviated FROM pay_third_party WHERE id=?")
			){
			pstmt.setLong(1, id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getString(1);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void update(final String code, final String name, final String abbreviated) {
		
		if(code == null || code.trim().isEmpty())
			throw new IllegalArgumentException("Le code doit être renseigné !");
		
		if(abbreviated == null || abbreviated.trim().isEmpty())
			throw new IllegalArgumentException("L'abrégé doit être renseigné !");
		
		if(this.codeIsUsed(code))
			throw new IllegalArgumentException("Ce code est déjà utilisé.");
			
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_third_party SET code=?, abbreviated=? WHERE id=?")
		) {
			super.update(name);
			pstmt.setString(1, code);
			pstmt.setString(2, abbreviated);
			pstmt.setLong(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public ThirdPartyFamily family() {
		try {
			final Long familyid = new JdbcSession(this.source)
				.sql(
	        		new Joined(
	    				" ",
	    				"SELECT family_id FROM pay_third_party",
	    				"WHERE id=?"
	    			).toString()
	    		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class));
			if(familyid.equals(0L)) {
				return ThirdPartyFamily.EMPTY;
			} else {
				return new DbThirdPartyFamily(this.source, familyid);
			}
			} catch(SQLException e) {
				throw new DatabaseException(e);
			}
	}
	
	@Override
	public void assign(ThirdPartyFamily family) {		
		try (
			final Connection connection = source.getConnection();
				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_third_party SET family_id=? WHERE id=?")
		) {
			if(family == ThirdPartyFamily.EMPTY) {
				pstmt.setObject(1, null);
			} else {
				pstmt.setLong(1, family.id());
			}
			
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public PaymentCondition paymentCondition() {
		return new DbPaymentCondition(this.source, this);
	}

	@Override
	public String toString() {
		return String.format("Code=%s, Intitulé=%s", this.code(), this.name());
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		final ThirdParty that = (ThirdParty)obj;
		return Objects.equal(this.id, that.id());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}
}
