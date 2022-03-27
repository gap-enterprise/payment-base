package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Payment;
import io.surati.gap.payment.api.PaymentExport;
import io.surati.gap.payment.api.PaymentStatus;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbPaymentExport implements PaymentExport {

	private final DataSource source;

	public DbPaymentExport(final DataSource source) {
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
                            "SELECT payment_id FROM pay_payment_export",
                            "WHERE is_done = false",
            				"ORDER BY id"
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
	public void add(final Payment payment) {
		if(!(payment.status() == PaymentStatus.ISSUED || payment.status() == PaymentStatus.CANCELLED)) {
			throw new IllegalArgumentException("Ce paiement ne peut être exportée !");
		}
		try {
			if(payment.status() == PaymentStatus.CANCELLED) {
				if (this.has(payment)) {
					new JdbcSession(this.source)
			            .sql(
			                new Joined(
			                    " ",
			                    "DELETE FROM pay_payment_export",
			                    "WHERE is_done = false AND payment_id=?"
			                ).toString()
			            )
			            .set(payment.id())
			            .execute();
				} else if (this.hasDone(payment)) {
					this.addDirect(payment);
				}
			} else {
				this.addDirect(payment);
			}      
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	private void addDirect(final Payment payment) {
		try {
			new JdbcSession(this.source)
	        .sql(
	            new Joined(
	                " ",
	                "INSERT INTO pay_payment_export (payment_id, is_done)",
	                "VALUES",
	                "(?, false)"
	            ).toString()
	        )
	        .set(payment.id())
	        .insert(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
	@Override
	public void finish() {
		try {
            new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "UPDATE pay_payment_export",
	                    "SET is_done = true",
	                    "WHERE is_done = false"
	                ).toString()
	            )
	            .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	private boolean has(final Payment payment) {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
	    				" ",
	    				"SELECT COUNT(*) FROM pay_payment_export",
	    				"WHERE is_done=false AND payment_id=?"
	    			).toString()
	    		)
				.set(payment.id())
	            .select(new SingleOutcome<>(Long.class)) > 0;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
	
	private boolean hasDone(final Payment payment) {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
	    				" ",
	    				"SELECT COUNT(*) FROM pay_payment_export",
	    				"WHERE is_done=true AND payment_id=?"
	    			).toString()
	    		)
				.set(payment.id())
	            .select(new SingleOutcome<>(Long.class)) > 0;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
}
