package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.BankNotes;
import io.surati.gap.payment.api.PaymentStatus;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbBankNotes implements BankNotes {

	/**
	 * Data Source.
	 */
	private final DataSource source;

	/**
	 * Ctor.
	 * @param source
	 */
	public DbBankNotes(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Iterable<BankNote> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM bank_note",
            				"ORDER BY id DESC"
                        ).toString()
                    )
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNote(
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
	public Iterable<BankNote> iterate(PaymentStatus status) {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT note.id FROM bank_note note",
							"LEFT JOIN payment pay ON pay.id=note.id",
                            "WHERE pay.status_id=?",
            				"ORDER BY id DESC"
                        ).toString()
                    )
                    .set(status.name())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNote(
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
	public BankNote get(Long id) {
		if(this.has(id)) {
			return new DbBankNote(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("La formule avec ID %s n'a pas été trouvée !", id));
		}
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM bank_note WHERE id=?")
				.set(id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql("SELECT COUNT(*) FROM bank_note")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

}
