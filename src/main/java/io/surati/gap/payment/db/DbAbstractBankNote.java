package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.User;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.commons.utils.amount.XofAmountInLetters;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.PaymentCancelReason;
import io.surati.gap.payment.api.PaymentStatus;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class DbAbstractBankNote extends DbAbstractPayment implements BankNote {

	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbAbstractBankNote(final DataSource source, final Long id) {
		super(source, id);
	}

	@Override
	public BankNoteBook book() {
		try {
			return new DbBankNoteBook(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT book_id FROM bank_note",
	        				"WHERE id=?"
	        			).toString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(Long.class))
            );
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public LocalDate dueDate() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT due_date FROM bank_note",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				final Date date = rs.getDate(1);
				if(date == null) {
					return null;
				} else {
					return date.toLocalDate();
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, final boolean sendbackinpayment, final User author) {
		if(author == User.EMPTY) {
			throw new IllegalArgumentException("Vous devez renseigner l'autheur du rejet !");
		}
		if(reason == PaymentCancelReason.NONE) {
			throw new IllegalArgumentException("Vous devez indiquer une raison");
		}
		if(canceldate == null) {
			throw new IllegalArgumentException("Vous devez renseigner la date d'annulation !");
		}
		super.cancel(canceldate, reason, description, sendbackinpayment, author);
	}

	@Override
	public String name() {
		return String.format("%s N°%s", this.book().meanType().toString(), this.issuerReference());
	}

	@Override
	public String amountInLetters() {
		return new XofAmountInLetters(this.amount()).toString();
	}

	@Override
	public String amountInHuman() {
		return new FrAmountInXof(this.amount()).toString();
	}

	@Override
	public void complete() {
		if(this.status() != PaymentStatus.TO_PRINT) {
			throw new IllegalArgumentException("Cette formule ne peut pas être finalisée !");
		}
		super.complete();
	}
}
