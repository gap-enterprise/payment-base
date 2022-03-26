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
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Bank;
import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.BankAccountAccountingSettings;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.BankNoteBookStatus;
import io.surati.gap.payment.api.PaymentMeanType;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Bank account in Database.
 * 
 * @since 3.0
 */
public final class DbBankAccount implements BankAccount {
	
	/**
	 * Data source.
	 */
	private final DataSource source;
	
	/**
	 * ID.
	 */
	private final Long id;

	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbBankAccount(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}

	@Override
	public Long id() {
		return this.id;
	}

	@Override
	public Bank bank() {
		try {
			return new DbBank(
				this.source,
				new JdbcSession(this.source)
			        .sql("SELECT bank_id FROM bank_account WHERE id=?")
			        .set(this.id)
			        .select(new SingleOutcome<>(Long.class))
	        );
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String branchCode() {
		try {
			return new JdbcSession(this.source)
		        .sql("SELECT branch_code FROM bank_account WHERE id=?")
		        .set(this.id)
		        .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String number() {
		try {
			return new JdbcSession(this.source)
		        .sql("SELECT number FROM bank_account WHERE id=?")
		        .set(this.id)
		        .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String key() {
		try {
			return new JdbcSession(this.source)
		        .sql("SELECT key FROM bank_account WHERE id=?")
		        .set(this.id)
		        .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void update(String branchcode, String number, String key) {
		final String fullnumber = String.format("%s%s%s%s", bank().code(), branchcode, number, key);
		if(fullnumber.length() != BankAccount.FULL_NUMBER_LENGTH) {
			throw new IllegalArgumentException("Le numéro complet du compte bancaire doit avoir exactement 24 caractères !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE bank_account",
                        "SET branch_code=?,number=?,key=?",
                        "WHERE id=?"
                    ).asString()
                )
                .set(branchcode)
                .set(number)
                .set(key)
                .set(this.id)
                .execute();
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public BankNoteBook addBook(final PaymentMeanType meantype, final String startnumber, final String endnumber, final String prefixnum) {
		try {
			final int istartnumber = Integer.valueOf(startnumber);
			final int iendnumber = Integer.valueOf(endnumber);		
			if(istartnumber <= 0) {
				throw new IllegalArgumentException("Le numéro de départ doit être supérieure à zéro !");
			}
			if(iendnumber <= 0) {
				throw new IllegalArgumentException("Le numéro de fin doit être supérieure à zéro !");
			}
			if(iendnumber < istartnumber) {
				throw new IllegalArgumentException("Le numéro de fin doit être supérieur ou égal au numéro de fin !");
			}
			final boolean overlapped = new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "SELECT COUNT(*) FROM bank_note_book",
	                    "WHERE (?='' OR prefix_number=?)",
	                    "AND (",
	                    "    	(? >= CAST(start_number AS integer) AND ? <= CAST(end_number AS integer))",
	                    "	 OR (? >= CAST(start_number AS integer) AND ? <= CAST(end_number AS integer))",
	                    ")",
	                    "AND mean_type_id=?",
	                    "AND account_id=?"
	                ).toString()
	            )
	            .set(prefixnum)
	            .set(prefixnum)
	            .set(istartnumber)
	            .set(istartnumber)
	            .set(iendnumber)
	            .set(iendnumber)
	            .set(meantype.name())
	            .set(this.id)
	            .select(new SingleOutcome<>(Long.class)) > 0;
	        if(overlapped) {
	        	throw new IllegalArgumentException("Certaines formules du carnet se chevauchent avec celles d'un carnet existant !");
	        }
	        return new DbBankNoteBook(
	            this.source,
	    		new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "INSERT INTO bank_note_book",
		                    "(account_id, mean_type_id, start_number, end_number, current_number, status_id, prefix_number)",
		                    "VALUES",
		                    "(?, ?, ?, ?, ?, ?, ?)"
		                ).toString()
		            )
		            .set(this.id)
		            .set(meantype.name())
		            .set(startnumber.trim())
		            .set(endnumber.trim())
		            .set(startnumber.trim())
		            .set(BankNoteBookStatus.REGISTERED.name())
		            .set(prefixnum)
		            .insert(new SingleOutcome<>(Long.class))
	        );
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    } catch (NumberFormatException ex) {
	        throw new IllegalArgumentException("Les numéros de début et de fin doivent comporter uniquement que des chiffres ! Vous pouvez utiliser le préfixe des numéros pour spécifier des caractères alphabétiques.");
	    }
	}

	@Override
	public BankNoteBook addBook(final PaymentMeanType meantype, String startnumber, final byte leafnumber, final String prefixnum) {
		return this.addBook(
			meantype,
			startnumber,
			this.format(
				startnumber,
				Integer.valueOf(startnumber) + leafnumber - 1
			),
			prefixnum
		);
	}

	@Override
	public String rib() {
		return String.format(
			"%s %s %s %s",
			this.bank().code(),
			this.branchCode(),
			this.number(),
			this.key()
		);
	}

	@Override
	public BankAccountAccountingSettings accountingSettings() {
		return new DbBankAccountAccountingSettings(this.source, this);
	}

	private String format(final String startnumber, final Integer number) {
		return StringUtils.leftPad(
			number.toString(),
			startnumber.length(),
			'0'
		);
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
		final BankAccount that = (BankAccount)obj;
		return Objects.equal(this.id, that.id());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}
}
