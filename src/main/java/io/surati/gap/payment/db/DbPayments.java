package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Payment;
import io.surati.gap.payment.api.PaymentStatus;
import io.surati.gap.payment.api.Payments;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbPayments implements Payments {

	/**
	 * Data Source.
	 */
	private final DataSource source;

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPayments(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Iterable<Payment> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment",
            				"ORDER BY id DESC"
                        ).toString()
                    )
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPayment(
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
	public Iterable<Payment> iterate(PaymentStatus status) {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment",
                            "WHERE status_id=?",
            				"ORDER BY id DESC"
                        ).toString()
                    )
                    .set(status.name())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPayment(
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
	public Payment get(Long id) {
		if(this.has(id)) {
			return new DbPayment(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le paiement avec ID %s n'a pas été trouvé !", id));
		}
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment WHERE id=?")
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
					.sql("SELECT COUNT(*) FROM pay_payment")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

}
