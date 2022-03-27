package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.BankNoteBooks;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public final class DbBankNoteBooks implements BankNoteBooks {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbBankNoteBooks(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Iterable<BankNoteBook> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_bank_note_book",
            				"ORDER BY id DESC"
                        ).asString()
                    )
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNoteBook(
                                this.source,
                                rset.getLong(1)
                            )
                        )
                    );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public Iterable<BankNoteBook> iterate(BankNoteBookStatus status) {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_bank_note_book",
                            "WHERE status_id=?",
            				"ORDER BY id DESC"
                        ).toString()
                    )
                    .set(status.name())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNoteBook(
                                this.source,
                                rset.getLong(1)
                            )
                        )
                    );
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public BankNoteBook get(Long id) {
		if(this.has(id)) {
			return new DbBankNoteBook(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le carnet avec ID %s n'a pas été trouvé !", id));
		}
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_bank_note_book WHERE id=?")
				.set(id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void remove(Long id) {
		final BankNoteBook book = this.get(id);
		if (book.status() != BankNoteBookStatus.REGISTERED) {
			throw new IllegalArgumentException(
				"Vous ne pouvez supprimer qu'un carnet non encore mis en utilisation !"
			);
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_bank_note_book",
                        "WHERE id=?"
                    ).toString()
                )
                .set(id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
	
	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql("SELECT COUNT(*) FROM pay_bank_note_book")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
