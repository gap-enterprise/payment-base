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
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.PaymentMeanType;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Bank note book from Database.
 * 
 * @since 3.0
 */
public abstract class DbAbstractBankNoteBook implements BankNoteBook {

	/**
	 * Data source.
	 */
	protected final DataSource source;
	
	/**
	 * Identifier.
	 */
	protected final Long id;
	
	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbAbstractBankNoteBook(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}
	
	@Override
	public Long id() {
		return id;
	}

	@Override
	public BankAccount account() {
		try {
			return new DbBankAccount(
				this.source,
				new JdbcSession(this.source)
			        .sql("SELECT account_id FROM pay_bank_note_book WHERE id=?")
			        .set(this.id)
			        .select(new SingleOutcome<>(Long.class))
	        );
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public PaymentMeanType meanType() {
		try {
			return PaymentMeanType.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT mean_type_id FROM pay_bank_note_book",
	        				"WHERE id=?"
	        			).asString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(String.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Iterable<BankNote> notesUsed() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_bank_note",
            				"WHERE book_id=? order by id DESC"
                        ).asString()
                    )
                    .set(this.id)
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNote(this.source, rset.getLong(1))
                        )
                    );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public BankNoteBookStatus status() {
		try {
			return BankNoteBookStatus.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT status_id FROM pay_bank_note_book",
	        				"WHERE id=?"
	        			).asString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(String.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void activate() {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_note_book",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(BankNoteBookStatus.IN_USE.name())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void block() {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_note_book",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(BankNoteBookStatus.BLOCKED.name())
                .set(this.id)
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void terminate() {
		if(this.hasNextNote()) {
			throw new IllegalArgumentException("Le carnet de formules ne peut pas être terminé : il possède encore des formules vides !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_note_book",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(BankNoteBookStatus.TERMINATED.name())
                .set(this.id)
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public int totalNumberOfNotes() {
		return this.iendNumber() - this.istartNumber() + 1;
	}

	@Override
	public int numberOfNotesUsed() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT COUNT(*) FROM pay_bank_note",
        				"WHERE book_id=?"
        			).asString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class))
	            .intValue();
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(String number) {
		final int num = Integer.valueOf(number.replaceFirst(this.prefixNumber(), ""));
		return num >= this.istartNumber() && num <= this.iendNumber();
	}

	private int istartNumber() {
		return Integer.valueOf(this.startNumber());
	}

	private int iendNumber() {
		return Integer.valueOf(this.endNumber());
	}

	public int icurrentNumber() {
		try {
			final String number = new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT current_number FROM pay_bank_note_book",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
			return Integer.valueOf(number);
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	/**
	 * Take current number.
	 * @return number
	 */
	@Override
	public synchronized String takeCurrentNumber() {
		if(this.status() != BankNoteBookStatus.IN_USE) {
			throw new IllegalArgumentException("Le carnet n'est pas en utilisation !");
		}
		final String current = this.currentNumber();
		try {
			if(this.hasNextNote()) {
				new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"UPDATE pay_bank_note_book",
        				"SET current_number=?",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.format(this.icurrentNumber() + 1))
				.set(this.id)
	            .update(Outcome.VOID);
			}
			return current;
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	@Override
	public boolean hasNextNote() {
		return this.totalNumberOfNotes() > this.numberOfNotesUsed();
	}

	@Override
	public void update(final String startnumber, final String endnumber) {
		if(this.status() != BankNoteBookStatus.REGISTERED) {
			throw new IllegalArgumentException("Le carnet ne peut pas être modifié !");
		}
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
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_note_book",
                        "SET start_number=?, end_number=?, current_number=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(startnumber.trim())
                .set(endnumber.trim())
                .set(startnumber.trim())
                .set(this.id)
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }  catch (NumberFormatException ex) {
	        throw new IllegalArgumentException("Les numéros de début et de fin doivent comporter uniquement que des chiffres ! Vous pouvez utiliser le préfixe des numéros pour spécifier des caractères alphabétiques.");
	    }
	}

	@Override
	public String startNumber() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT start_number FROM pay_bank_note_book",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String endNumber() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT end_number FROM pay_bank_note_book",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String currentNumber() {
		return String.format("%s%s", this.prefixNumber(), this.format(this.icurrentNumber()));
	}
	
	@Override
	public String name() {
		return String.format(
			"%s - %s - %s%s:%s%s",
			this.account().bank().abbreviated(),
			this.account().rib(),
			this.prefixNumber(),
			this.startNumber(),
			this.prefixNumber(),
			this.endNumber()
		);
	}

	@Override
	public String prefixNumber() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT prefix_number FROM pay_bank_note_book",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	private String format(final Integer number) {
		return StringUtils.leftPad(number.toString(), this.startNumber().length(), '0');
	}

	@Override
	public void prefixNumber(String prefix) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_note_book",
                        "SET prefix_number=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(prefix.trim())
                .set(this.id)
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public synchronized String nextNoteAfter(String number) {
		if(this.status() != BankNoteBookStatus.IN_USE) {
			throw new IllegalArgumentException("Le carnet n'est pas en utilisation !");
		}
		final String nextcurrent;
		if(StringUtils.isBlank(number)) {
			nextcurrent = this.currentNumber();
		} else {
			final int inumber = Integer.parseInt(number.replaceFirst(this.prefixNumber(), ""));
			if(StringUtils.isBlank(this.prefixNumber())) {
				nextcurrent = this.format(inumber + 1);
			} else {
				nextcurrent = String.format("%s%s", this.prefixNumber(), this.format(inumber + 1));
			}
		}
		return nextcurrent;
	}

	@Override
	public boolean hasNextNoteAfter(String number) {
		return this.has(this.nextNoteAfter(number));
	}
}
