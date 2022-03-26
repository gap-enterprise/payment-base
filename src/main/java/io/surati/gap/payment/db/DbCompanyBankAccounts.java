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
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Bank;
import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.BankAccounts;
import io.surati.gap.payment.api.PaymentMeanType;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Bank accounts of current company in Database.
 * 
 * @since 3.0
 */
public final class DbCompanyBankAccounts implements BankAccounts {
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbCompanyBankAccounts(final DataSource source) {
		this.source = source;
	}

	@Override
	public boolean has(String fullnumber) {
		try {
			return new JdbcSession(this.source)
		        .sql(
	        		new Joined(
        				" ",
        				"SELECT COUNT(*) FROM bank_account_view",
        				"WHERE holder_id IS NULL AND full_number = ?"
        			).asString()
        		)
		        .set(fullnumber)
		        .select(new SingleOutcome<>(Integer.class)) > 0;
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccount get(String fullnumber) {
		if(!has(fullnumber)) {
			throw new IllegalArgumentException(
				String.format(
					"Le compte bancaire N°%s est introuvable !",
					fullnumber
				)
			);
		}
		try {
			return new DbBankAccount(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT id FROM bank_account_view",
	        				"WHERE holder_id IS NULL AND full_number = ?"
	        			).asString()
	        		)
			        .set(fullnumber)
			        .select(new SingleOutcome<>(Long.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccount get(Long id) {
		try {
			return new DbBankAccount(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT id FROM bank_account",
	        				"WHERE holder_id IS NULL AND id=?"
	        			).asString()
	        		)
			        .set(id)
			        .select(new SingleOutcome<>(Long.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccount add(
		final Bank bank,
		final String branchcode,
		final String number,
		final String key
	) {
		final String fullnumber = String.format("%s%s%s%s", bank.code(), branchcode, number, key);
		if(fullnumber.length() != BankAccount.FULL_NUMBER_LENGTH) {
			throw new IllegalArgumentException("Le numéro complet du compte bancaire doit avoir exactement 24 caractères !");
		}
		try {
			final JdbcSession session = new JdbcSession(this.source);
            final BankAccount account = new DbBankAccount(
                this.source,     
                session.sql(
                        new Joined(
                            " ",
                            "INSERT INTO bank_account",
                            "(branch_code, number, key, bank_id)",
                            "VALUES",
                            "(?, ?, ?, ?)"
                        ).toString()
                    )
                    .set(branchcode)
                    .set(number)
                    .set(key)
                    .set(bank.id())
                    .insert(new SingleOutcome<>(Long.class))
            );
            for (PaymentMeanType meantype : PaymentMeanType.values()) {
            	if(meantype == PaymentMeanType.NONE) {
            		continue;
            	}
            	session
                	.sql(
	                    new Joined(
	                        " ",
	                        "INSERT INTO bank_account_accounting_setting",
	                        "(account_id, mean_type_id)",
	                        "VALUES",
	                        "(?, ?)"
	                    ).toString()
	                )
	                .set(account.id())
	                .set(meantype.name())
	                .insert(Outcome.VOID);
			}
            return account;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void remove(Long id) {
		try {
			final boolean used = new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "SELECT COUNT(*) FROM bank_account",
	                    "WHERE id=? AND (id IN (SELECT account_id FROM bank_note_book)",
	                    "OR id IN (SELECT account_id FROM payment_batch))"
	                ).toString()
	            )
	            .set(id)
	            .select(new SingleOutcome<>(Long.class)) > 0;
	        if(used) {
	        	throw new IllegalArgumentException("Le compte ne peut pas être supprimé (déjà utilisé) !");
	        }
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM bank_account",
                        "WHERE holder_id IS NULL AND id=?"
                    ).asString()
                )
                .set(id)
                .execute();
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public Iterable<BankAccount> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM bank_account",
            				"WHERE holder_id IS NULL"
                        ).asString()
                    )
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankAccount(
                                this.source,
                                rset.getLong(1)
                            )
                        )
                    );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}
	
	
}
