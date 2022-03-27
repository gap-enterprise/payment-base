package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentMeans;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public final class DbPaymentMeans implements PaymentMeans {

	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	/**
	 * Bank.
	 */
	private final Bank bank;

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaymentMeans(final DataSource source, final Bank bank) {
		this.source = source;
		this.bank = bank;
	}

	@Override
	public Iterable<PaymentMean> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment_mean",
            				"WHERE bank_id=? order by id"
                        ).asString()
                    )
                    .set(this.bank.id())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPaymentMean(
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
	public PaymentMean get(Long id) {
		if(this.has(id)) {
			return new DbPaymentMean(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le moyen de paiement avec ID %s n'a pas été trouvé !", id));
		}
	}

	@Override
	public PaymentMean get(PaymentMeanType type) {
		if(this.has(type)) {
			try {
				return new DbPaymentMean(
					this.source, new JdbcSession(this.source)
						.sql(
			        		new Joined(
		        				" ",
		        				"SELECT id FROM pay_payment_mean",
		        				"WHERE bank_id=? AND type_id=?"
		        			).toString()
		        		)
						.set(this.bank.id())
						.set(type.name())
			            .select(new SingleOutcome<>(Long.class)));
			} catch(SQLException ex) {
				throw new DatabaseException(ex);
			}		
		} else {
			throw new IllegalArgumentException("Le moyen de paiement n'a pas été trouvé !");
		}
	}
	
	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_mean WHERE id=?")
				.set(id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	private boolean has(final PaymentMeanType type) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_mean WHERE bank_id=? AND type_id=?")
				.set(this.bank.id())
				.set(type.name())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
