package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Payment;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.wp.PaymentWrapper;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbPayment extends PaymentWrapper {

	public DbPayment(final DataSource source, final Long id) {
		super(DbPayment.get(source, id));
	}

	private static Payment get(final DataSource source, final Long id) {
		final Payment payment;
		try {
			final PaymentMeanType type =
				PaymentMeanType.valueOf(new JdbcSession(source)
					.sql(
						new Joined(
							" ",
							"SELECT mean_type_id FROM payment",
							"WHERE id=?"
						).toString()
					)
					.set(id)
					.select(new SingleOutcome<>(String.class)));
			if (type.isBankNote()) {
				payment = new DbBankNote(source, id);
			} else {
				throw new IllegalArgumentException("Ce moyen de paiement n'est pas encore pris en charge !");
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		return payment;
	}
}
