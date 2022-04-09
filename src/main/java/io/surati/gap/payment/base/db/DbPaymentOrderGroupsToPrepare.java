package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ThirdParty;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.LengthOf;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class DbPaymentOrderGroupsToPrepare implements PaymentOrderGroupsToPrepare {

	private final DataSource source;

	private final User user;

	DbPaymentOrderGroupsToPrepare(final DataSource source, final User user) {
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
                            "SELECT grp.id FROM pay_payment_order_group grp",
                            "LEFT JOIN pay_third_party tp on tp.id=grp.third_party_id",
                            "WHERE grp.status_id=? AND (?=true OR worker_id=?)",
            				"ORDER BY tp.abbreviated ASC, grp.id DESC"
                        ).toString()
                    )
                    .set(PaymentOrderStatus.TO_PREPARE.name())
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
	public PaymentOrderGroup merge(final Iterable<PaymentOrder> orders) {
		if(new LengthOf(orders).intValue() == 0) {
			throw new IllegalArgumentException("Il n'y a aucun ordre de paiement à fusionner !");
		}
		final List<ThirdParty> tps = new LinkedList<>();
		for (PaymentOrder po : orders) {
			final ThirdParty tp = po.beneficiary();
			if(!tps.contains(tp)) {
				tps.add(tp);
			}
		}
		if(tps.size() > 1) {
			throw new IllegalArgumentException("Les ordres de paiement doivent appartenir au même bénéficiaire !");
		}
		final ThirdParty tp = tps.get(0);
		final PaymentOrderGroup group = this.add(tp, false);
		for (PaymentOrder po : orders) {
			this.merge(group, po);
		}

		this.removeEmptyGroups();
		return group;
	}

	@Override
	public void validate() {
		for (PaymentOrderGroup group : this.iterate()) {
			group.validate(user);
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
	public void merge(final PaymentOrderGroup group, final Iterable<PaymentOrder> orders) {
		for (PaymentOrder po : orders) {
			this.merge(group, po);
		}
	}

	@Override
	public void merge(PaymentOrderGroup group, PaymentOrder order) {
		if(group.has(order)) {
			return;
		}
		if(order.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("L'ordre de paiement à ajouter au groupe doit être en préparation !");
		}
		final ThirdParty beneficiary = group.beneficiary();
		if(!group.isHetero() && !beneficiary.equals(order.beneficiary())) {
			throw new IllegalArgumentException(String.format("Les ordres de paiement doivent appartenir au même bénéficiaire (%s)!", beneficiary.abbreviated()));
		}
		try {
			new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"DELETE FROM pay_payment_order_group_line",
						"WHERE id=?"
					).toString()
				)
				.set(order.id())
				.execute();
			new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"INSERT INTO pay_payment_order_group_line (id, group_id) VALUES",
						"(?, ?)"
					).toString()
				)
				.set(order.id())
				.set(group.id())
				.execute();
			this.removeEmptyGroups();
	    } catch (SQLException ex) {
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

	private PaymentOrderGroup add(final ThirdParty tp, final boolean ishetero) {
		try {
			final PaymentOrderGroup group = new DbPaymentOrderGroup(
				this.source,
				new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "INSERT INTO pay_payment_order_group",
		                    "(third_party_id, status_id, author_id, worker_id, is_hetero)",
		                    "VALUES",
		                    "(?, ?, ?, ?, ?)"
		                ).toString()
		            )
		            .set(tp.id())
		            .set(PaymentOrderStatus.TO_PREPARE.name())
		            .set(user.id())
		            .set(user.id())
		            .set(ishetero)
		            .insert(new SingleOutcome<>(Long.class))
			);
			final Iterable<PaymentMeanType> meantypes = tp.paymentCondition().meanTypesAllowed();
			if(
				new LengthOf(meantypes).intValue() == 1
			) {
				final PaymentMeanType meantype = meantypes.iterator().next();
				group.update(meantype, LocalDate.MIN);
			}
			return group;
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
	}

	private boolean has(final Long id) {
		return new ListOf<>(this.iterate())
			.stream()
			.anyMatch(g -> g.id().equals(id));
	}
	
	private void removeEmptyGroups() {
		try {
			new JdbcSession(this.source)
            .sql(
                new Joined(
                    " ",
                    "DELETE FROM pay_payment_order_group",
                    "WHERE id NOT IN (SELECT group_id FROM pay_payment_order_group_line)"
                ).toString()
            )
            .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Iterable<PaymentOrderGroup> prepare(final Iterable<ReferenceDocument> documents) {
		final Collection<PaymentOrder> orders = new LinkedList<>();
		for (ReferenceDocument doc : documents) {
			if(doc.step() != ReferenceDocumentStep.SELECTED) {
				throw new IllegalArgumentException("Le document de référence doit être sélectionné avant d'être envoyé en préparation !");
			}
			final PaymentOrder order = doc.preparePayment(user);
			orders.add(order);
		}
		final List<ThirdParty> tps = new LinkedList<>();
		for (PaymentOrder po : orders) {
			final ThirdParty tp = po.beneficiary();
			if(!tps.contains(tp)) {
				tps.add(tp);
			}
		}
		final Collection<PaymentOrderGroup> groups = new LinkedList<>();
		for (ThirdParty tp : tps) {
			final PaymentOrderGroup group = this.add(tp, false);
			groups.add(group);
			for (PaymentOrder po : orders) {
				if(tp.equals(po.beneficiary())) {
					this.merge(group, po);
				}
			}
		}
		this.removeEmptyGroups();
		return groups;
	}

	@Override
	public PaymentOrderGroup mergeAcross(ThirdParty beneficiary, Iterable<PaymentOrder> orders) {
		final PaymentOrderGroup group = this.add(beneficiary, true);
		for (PaymentOrder po : orders) {
			this.merge(group, po);
		}
		this.removeEmptyGroups();
		return group;
	}
}
