package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.wp.BankNoteWrapper;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbBankNote extends BankNoteWrapper {

	public DbBankNote(final DataSource source, final Long id) {
		super(DbBankNote.get(source, id));
	}

	private static BankNote get(final DataSource source, final Long id) {
		final BankNote note;
		try {
			final PaymentMeanType type =
				PaymentMeanType.valueOf(new JdbcSession(source)
					.sql(
						new Joined(
							" ",
							"SELECT book.mean_type_id FROM bank_note as note",
							"LEFT JOIN bank_note_book as book ON book.id = note.book_id",
							"WHERE note.id=?"
						).toString()
					)
					.set(id)
					.select(new SingleOutcome<>(String.class)));
			
			switch (type) {
				case CHEQUE:
					note = new DbCheck(source, id);
					break;
				case LETTRE_DE_CHANGE:
					note = new DbBillOfExchange(source, id);
					break;
				case BILLET_A_ORDRE:
					note = new DbPromissoryNote(source, id);
					break;
				default:
					throw new IllegalArgumentException("L'effet bancaire de ce moyen de paiement n'est pas encore supporté !");
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		return note;
	}

}
