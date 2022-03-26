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

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Bank;
import io.surati.gap.payment.api.Banks;
import io.surati.gap.payment.api.PaymentMean;
import io.surati.gap.payment.api.PaymentMeanFieldType;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.ThirdParties;
import io.surati.gap.payment.api.ThirdParty;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Banks in Database.
 * 
 * @since 3.0
 */
public final class DbBanks implements Banks {
	
	/**
	 * Third parties.
	 */
	private final ThirdParties tps;
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	/**
	 * Ctor.
	 * @param source
	 */
	public DbBanks(final DataSource source) {
		this.tps = new DbThirdParties(source);
		this.source = source;
	}
	
	@Override
	public Bank get(String code) {
		try(
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM third_party WHERE code=? and id IN (select id from bank)");
		){
			pstmt.setString(1, code);		
			try(final ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					Long id = rs.getLong(1);
					return new DbBank(source, id);
				} else {
					throw new IllegalArgumentException(String.format("La banque avec le code %s est introuvable !", code));
				}			
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public Bank get(Long id) {
		try(
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM third_party WHERE id=? and id in (select id FROM bank)");
		){
			pstmt.setLong(1, id);		
			try(final ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					return new DbBank(source, id);
				} else {
					throw new IllegalArgumentException(String.format("La banque avec l'ID %s est introuvable!", id));
				}			
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public Bank add(String code, String name, String abbreviated) {
		if(!StringUtils.isBlank(code) && code.length() != 5) {
			throw new IllegalArgumentException("La longueur du code d'une banque doit être exactement de 5 caractères !");
		}
		final ThirdParty tp = this.tps.add(code, name, abbreviated);
		final Bank bank;
		try(
			final Connection connection = this.source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("INSERT INTO bank (id) VALUES (?)")
		){
			pstmt.setLong(1, tp.id());
			pstmt.executeUpdate();			
			bank = new DbBank(this.source, tp);		
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
		this.configurePaymentMeans(bank);
		return bank;
	}
	
	private void configurePaymentMeans(final Bank bank) {
		for (PaymentMeanType type : PaymentMeanType.values()) {
			if(!type.equals(PaymentMeanType.NONE)) {
				this.createPaymentMean(bank, type);
			}
		}
	}

	private void createPaymentMean(final Bank bank, final PaymentMeanType type) {
		final PaymentMean mean;
		try {
			 mean = new DbPaymentMean(
			    this.source,
				new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "INSERT INTO payment_mean",
	                    "(type_id, bank_id, image_file_name, width, height, dpi)",
	                    "VALUES",
	                    "(?, ?, ?, ?, ?, ?)"
	                ).asString()
	            )
	            .set(type.name())
	            .set(bank.id())
	            .set("check_sample.jpg")
	            .set(17.5)
	            .set(8)
	            .set(200)
	            .insert(new SingleOutcome<>(Long.class))
            );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
		this.generateFields(mean);
	}
	
	private void generateFields(final PaymentMean mean) {
		this.createPaymentMeanField(mean, PaymentMeanFieldType.MONTANT_EN_CHIFFRES, 14, 0.9, 3.2);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.MONTANT_EN_LETTRES_LIGNE_1, 4.5, 1.5, 12.7);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.MONTANT_EN_LETTRES_LIGNE_2, 0.7, 2.2, 16.5);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.BENEFICIAIRE, 2.6, 2.8, 14.5);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.DATE_EDITION, 14.6, 3.4, 2.5);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.VILLE_EDITION, 11.3, 3.4, 2.5);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.MENTION_SUPPLEMENTAIRE_1, 0, 35, 0);
		this.createPaymentMeanField(mean, PaymentMeanFieldType.MENTION_SUPPLEMENTAIRE_2, 0, 35, 0);
		if(mean.type() == PaymentMeanType.LETTRE_DE_CHANGE || mean.type() == PaymentMeanType.BILLET_A_ORDRE) {
			this.createPaymentMeanField(mean, PaymentMeanFieldType.DATE_ECHEANCE, 7.5, 3.4, 2.5);
		}
	}

	private void createPaymentMeanField(final PaymentMean mean, final PaymentMeanFieldType type, final double x, final double y, final double width) {
		try {
			new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "INSERT INTO payment_mean_field",
	                    "(type_id, mean_id, x, y, width, visible)",
	                    "VALUES",
	                    "(?, ?, ?, ?, ?, ?)"
	                ).asString()
	            )
	            .set(type.name())
	            .set(mean.id())
	            .set(x)
	            .set(y)
	            .set(width)
	            .set(type == PaymentMeanFieldType.MENTION_SUPPLEMENTAIRE_1 || type == PaymentMeanFieldType.MENTION_SUPPLEMENTAIRE_2 ? false : true)
	            .insert(Outcome.VOID);
        } catch (SQLException | IOException e) {
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
	                    "SELECT COUNT(*) FROM bank",
	                    "WHERE id=? AND (id IN (SELECT bank_id FROM bank_account)",
	                    "OR id IN (SELECT beneficiary_id FROM payment_order)",
	                    "OR id IN (SELECT third_party_id FROM payment_order_group)",
	                    "OR id IN (SELECT beneficiary_id FROM bank_note)",
	                    "OR id IN (SELECT issuer_id FROM reference_document))"
	                ).toString()
	            )
	            .set(id)
	            .select(new SingleOutcome<>(Long.class)) > 0;
	        if(used) {
	        	throw new IllegalArgumentException("La banque ne peut pas être supprimée (déjà utilisée) !");
	        }
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("DELETE FROM person WHERE id=?");
		) {
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public Iterable<Bank> iterate() {		
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM bank")
		){
			final Collection<Bank> items = new ArrayList<>();		
			try(final ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					items.add(new DbBank(source, rs.getLong(1)));
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
			final PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) as nb FROM third_party WHERE code=? and id IN (select id from bank)")
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
					.sql("SELECT COUNT(*) FROM bank")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
