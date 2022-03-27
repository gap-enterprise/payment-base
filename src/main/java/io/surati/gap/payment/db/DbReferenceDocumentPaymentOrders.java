package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.PaymentOrder;
import io.surati.gap.payment.api.ReferenceDocument;
import io.surati.gap.payment.api.ThirdPartyPaymentOrders;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbReferenceDocumentPaymentOrders implements ThirdPartyPaymentOrders {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	private final ReferenceDocument rd;
	
	private final ThirdPartyPaymentOrders tporders;
	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbReferenceDocumentPaymentOrders(final DataSource source, final ReferenceDocument rd) {
		this.source = source;
		this.rd = rd;
		this.tporders = new DbThirdPartyPaymentOrders(source, this.rd.issuer());
	}

	@Override
	public Iterable<PaymentOrder> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment_order",
                            "WHERE reference_document_id=?",
            				"ORDER BY date, id ASC"
                        ).toString()
                    )
                    .set(this.rd.id())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPaymentOrder(
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
	public PaymentOrder add(double amounttopay, String reason, String description, User author) {
		final PaymentOrder order = tporders.add(amounttopay, reason, description, author);
		order.joinTo(this.rd);
		return order;
	}

	@Override
	public PaymentOrder add(String reference, double amounttopay, String reason, String description, User author) {
		final PaymentOrder order = tporders.add(reference, amounttopay, reason, description, author);
		order.joinTo(this.rd);
		return order;
	}
	
	@Override
	public PaymentOrder get(Long id) {
		if(this.has(id)) {
			return new DbPaymentOrder(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement avec ID %s n'a pas été trouvé !", id));
		}
	}

	@Override
	public void remove(final PaymentOrder item) {
		if(!this.rd.id().equals(item.document().id())) {
			throw new IllegalArgumentException("L'ordre de paiement à supprimer n'appartient pas au document de référence !");
		}
		this.tporders.remove(item);
	}

	@Override
	public Long count() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE reference_document_id=?")
				.set(this.rd.id())
				.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE id=? AND reference_document_id=?")
				.set(id)
				.set(this.rd.id())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(String reference) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE reference=? AND reference_document_id=?")
				.set(reference.trim())
				.set(this.rd.id())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double totalAmount() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT SUM(amount_to_pay) FROM pay_payment_order WHERE reference_document_id=?")
				.set(this.rd.id())
				.select(new SingleOutcome<>(Long.class)).doubleValue();
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public PaymentOrder get(String reference) {
		if(this.has(reference)) {
			try {
				return new DbPaymentOrder(
					this.source,
					new JdbcSession(this.source)
						.sql("SELECT id FROM pay_payment_order WHERE reference_document_id=? AND reference=?")
						.set(this.rd.id())
						.set(reference)
						.select(new SingleOutcome<>(Long.class))
				);
			} catch(SQLException ex) {
				throw new DatabaseException(ex);
			}
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement avec Référence %s n'a pas été trouvé !", reference));
		}
	}
}
