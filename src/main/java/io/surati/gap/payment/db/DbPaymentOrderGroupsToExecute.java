package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentOrderGroupsToExecute;
import io.surati.gap.payment.api.PaymentOrderStatus;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbPaymentOrderGroupsToExecute implements PaymentOrderGroupsToExecute {

	private final DataSource source;
	
	private final User user;

	DbPaymentOrderGroupsToExecute(final DataSource source, final User user) {
		this.source = source;
		this.user = user;
	}

	@Override
	public Iterable<PaymentOrderGroup> iterate() {
		final boolean canviewall = false;
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment_order_group",
                            "WHERE status_id=? AND (?=true OR worker_id=?)",
            				"ORDER BY third_party_id ASC"
                        ).toString()
                    )
                    .set(PaymentOrderStatus.IN_WAITING_FOR_PAYMENT.name())
                    .set(canviewall)
                    .set(user.id())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPaymentOrderGroup(
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
	public PaymentOrderGroup get(Long id) {
		if(this.has(id)) {
			return new DbPaymentOrderGroup(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le groupe d'ordre de paiement (ID=%s) n'a pas été trouvé !", id));
		}
	}

	@Override
	public void sendBackInPreparation() {
		for (PaymentOrderGroup group : this.iterate()) {
			group.sendBackInPreparation();
		}
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order_group WHERE id=? AND status_id=?")
				.set(id)
				.set(PaymentOrderStatus.IN_WAITING_FOR_PAYMENT.name())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double totalAmount() {
		Double amount = 0.0;
		for (PaymentOrderGroup item : this.iterate()) {
			amount += item.totalAmount();
		}
		return amount;
	}
}
