package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.PaymentCondition;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.ThirdParty;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbPaymentCondition implements PaymentCondition {

	private final DataSource source;
	
	private final ThirdParty tp;

	public DbPaymentCondition(final DataSource source, final ThirdParty tp) {
		this.source = source;
		this.tp = tp;
	}

	@Override
	public int deadline() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT payment_deadline FROM pay_third_party",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.tp.id())
	            .select(new SingleOutcome<>(Long.class))
	            .intValue();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void update(int deadline) {
		if(deadline < 0) {
			throw new IllegalArgumentException("Le délai de paiement doit être positif !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_third_party",
                        "SET payment_deadline=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(deadline)
				.set(this.tp.id())
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public String toString() {
		return String.format("Tiers=%s, Délai=%s jrs", this.tp.code(), this.deadline());
	}

	@Override
	public ThirdParty thirdParty() {
		return this.tp;
	}

	@Override
	public Iterable<PaymentMeanType> meanTypesAllowed() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT mean_type_id FROM pay_third_party_payment_mean_allowed",
                            "WHERE third_party_id=?",
            				"ORDER BY mean_type_id"
                        ).toString()
                    )
                    .set(this.tp.id())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            PaymentMeanType.valueOf(rset.getString(1))
                        )
                    );
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void add(PaymentMeanType meantype) {
		if(this.has(meantype)) {
			return;
		}
		try {
			new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "INSERT INTO pay_third_party_payment_mean_allowed",
	                    "(mean_type_id, third_party_id)",
	                    "VALUES",
	                    "(?, ?)"
	                ).toString()
	            )
	            .set(meantype.name())
	            .set(this.tp.id())
	            .insert(Outcome.VOID);
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
	}

	@Override
	public void remove(PaymentMeanType meantype) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_third_party_payment_mean_allowed",
                        "WHERE mean_type_id=? AND third_party_id=?"
                    ).toString()
                )
                .set(meantype.name())
                .set(this.tp.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public boolean has(PaymentMeanType meantype) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_third_party_payment_mean_allowed WHERE mean_type_id=? AND third_party_id=?")
				.set(meantype.name())
				.set(this.tp.id())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
