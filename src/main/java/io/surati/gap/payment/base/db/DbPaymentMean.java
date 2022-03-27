package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.Image;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanField;
import io.surati.gap.payment.base.api.PaymentMeanFieldType;
import io.surati.gap.payment.base.api.PaymentMeanType;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public final class DbPaymentMean implements PaymentMean {

	private final DataSource source;
	
	private final Long id;
	
	public DbPaymentMean(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}
	
	@Override
	public Long id() {
		return this.id;
	}

	@Override
	public PaymentMeanType type() {
		try {
			return PaymentMeanType.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT type_id FROM pay_payment_mean",
	        				"WHERE id=?"
	        			).asString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(String.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Bank bank() {
		try {
			return new DbBank(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT bank_id FROM pay_payment_mean",
	        				"WHERE id=?"
	        			).asString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(Long.class))
	        );
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Image image() {
		return new DbImage(this.source, this.id);
	}

	@Override
	public Iterable<PaymentMeanField> fields() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT type_id FROM pay_payment_mean_field",
            				"WHERE mean_id=? ORDER BY type_id"
                        ).asString()
                    )
                    .set(this.id)
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPaymentMeanField(
                                this.source,
                                PaymentMeanFieldType.valueOf(rset.getString(1)),
                                new DbPaymentMean(this.source, this.id)
                            )
                        )
                    );
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public PaymentMeanField field(PaymentMeanFieldType type) {
		try {
			final Collection<PaymentMeanField> fields = new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT * FROM pay_payment_mean_field",
        				"WHERE type_id=? AND mean_id=?"
        			).asString()
        		)
				.set(type.name())
				.set(this.id)
	            .select(
            		new ListOutcome<>(
        				rset ->
        				new DbPaymentMeanField(
    						this.source,
    						type,
    						this
						)
    				)
        		);
			if(fields.isEmpty()) {
				throw new IllegalArgumentException("Champ de moyen de paiement introuvable !");
			} else {
				return new DbPaymentMeanField(this.source, type, this);
			}
		} catch (SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String name() {
		return this.type().toString();
	}

	@Override
	public String toString() {
		return String.format("ID=%s, %s, %s", this.id, this.type().name(), this.bank().abbreviated());
	}
}
