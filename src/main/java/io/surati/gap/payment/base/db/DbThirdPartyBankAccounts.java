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
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankAccounts;
import io.surati.gap.payment.base.api.ThirdParty;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Bank accounts of a third party in Database.
 * 
 * @since 3.0
 */
public final class DbThirdPartyBankAccounts implements BankAccounts {
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	/**
	 * Holder of accounts.
	 */
	private final ThirdParty holder;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbThirdPartyBankAccounts(final DataSource source, final ThirdParty holder) {
		this.source = source;
		this.holder = holder;
	}

	@Override
	public boolean has(String fullnumber) {
		try {
			return new JdbcSession(this.source)
		        .sql(
	        		new Joined(
        				" ",
        				"SELECT COUNT(*) FROM pay_bank_account_view",
        				"WHERE holder_id=? AND full_number = ?"
        			).asString()
        		)
		        .set(this.holder.id())
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
	        				"SELECT id FROM pay_bank_account_view",
	        				"WHERE holder_id=? AND full_number = ?"
	        			).asString()
	        		)
					.set(this.holder.id())
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
			final List<Long> results =
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT id FROM pay_bank_account",
	        				"WHERE holder_id=? AND id=?"
	        			).asString()
	        		)
					.set(this.holder.id())
			        .set(id)
		            .select(
		                new ListOutcome<>(
		                    rset -> rset.getLong(1)
		                )
		            );
			if(results.isEmpty()) {
				throw new IllegalArgumentException(
					String.format(
						"Le compte bancaire avec l'identifiant %s est introuvable !",
						id
					)
				);
			}
			return new DbBankAccount(
				this.source,
				results.get(0)
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
		try {
			final String fullnumber = String.format("%s%s%s%s", bank.code(), branchcode, number, key);
			if(fullnumber.length() != BankAccount.FULL_NUMBER_LENGTH) {
				throw new IllegalArgumentException("Le numéro complet du compte bancaire doit avoir exactement 24 caractères !");
			}
            return new DbBankAccount(
                this.source,
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "INSERT INTO pay_bank_account",
                            "(branch_code, number, key, bank_id, holder_id)",
                            "VALUES",
                            "(?, ?, ?, ?, ?)"
                        ).asString()
                    )
                    .set(branchcode)
                    .set(number)
                    .set(key)
                    .set(bank.id())
                    .set(this.holder.id())
                    .insert(new SingleOutcome<>(Long.class))
            );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public void remove(Long id) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_bank_account",
                        "WHERE holder_id=? AND id=?"
                    ).asString()
                )
                .set(this.holder.id())
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
                            "SELECT id FROM pay_bank_account",
            				"WHERE holder_id=?"
                        ).asString()
                    )
                    .set(this.holder.id())
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
