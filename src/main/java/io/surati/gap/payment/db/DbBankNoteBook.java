package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.wp.BankNoteBookWrapper;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbBankNoteBook extends BankNoteBookWrapper {

	public DbBankNoteBook(final DataSource source, final Long id) {
		super(DbBankNoteBook.get(source, id));
	}

	private static BankNoteBook get(final DataSource source, final Long id) {
		final BankNoteBook book;
		try {
			final PaymentMeanType type =
				PaymentMeanType.valueOf(new JdbcSession(source)
					.sql(
						new Joined(
							" ",
							"SELECT mean_type_id FROM pay_bank_note_book",
							"WHERE id=?"
						).toString()
					)
					.set(id)
					.select(new SingleOutcome<>(String.class)));
			
			switch (type) {
				case CHEQUE:
					book = new DbCheckBook(source, id);
					break;
				case LETTRE_DE_CHANGE:
					book = new DbBillOfExchangeBook(source, id);
					break;
				case BILLET_A_ORDRE:
					book = new DbPromissoryNoteBook(source, id);
					break;
				default:
					throw new IllegalArgumentException("Le carnet de ce moyen de paiement n'est pas encore supporté !");
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		return book;
	}
}
