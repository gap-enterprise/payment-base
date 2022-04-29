package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.Company;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.ThirdPartyPaymentOrders;
import org.apache.commons.lang.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;

public final class DbThirdPartyPaymentOrders implements ThirdPartyPaymentOrders {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	private final ThirdParty tp;
	
	private final PaymentOrderStatus status;

	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbThirdPartyPaymentOrders(final DataSource source, final ThirdParty tp) {
		this(source, tp, PaymentOrderStatus.NONE);
	}

	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbThirdPartyPaymentOrders(final DataSource source, final ThirdParty tp, final PaymentOrderStatus status) {
		this.source = source;
		this.tp = tp;
		this.status = status;
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
                            "WHERE beneficiary_id=? AND (?='NONE' OR status_id=?)",
            				"ORDER BY date, id DESC"
                        ).toString()
                    )
                    .set(this.tp.id())
                    .set(this.status.name())
                    .set(this.status.name())
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
		String reference = this.generateReference();
		while(this.has(reference)) {
			reference = this.generateReference();
		}
		return this.add(reference, amounttopay, reason, description, author);
	}

	@Override
	public PaymentOrder add(String reference, double amounttopay, String reason, String description, User author) {
		if(StringUtils.isBlank(reason)) {
			throw new IllegalArgumentException("Vous devez donner un motif de règlement !");
		}
		if(StringUtils.isBlank(reference)) {
			throw new IllegalArgumentException("Vous devez renseigner le référence de l'ordre !");
		}
		if(this.has(reference)) {
			throw new IllegalArgumentException(String.format("La référence de l'ordre (%s) est déjà utilisée !", reference));
		}
		try {
			return new DbPaymentOrder(
	            this.source,
	    		new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "INSERT INTO pay_payment_order",
		                    "(date, reference, beneficiary_id, amount, author_id, status_id, reason, description)",
		                    "VALUES",
		                    "(?, ?, ?, ?, ?, ?, ?, ?)"
		                ).toString()
		            )
		            .set(java.sql.Date.valueOf(LocalDate.now()))
		            .set(reference)
		            .set(this.tp.id())
		            .set(amounttopay)
		            .set(author.id())
		            .set(PaymentOrderStatus.TO_PREPARE.name())
		            .set(reason)
		            .set(description)
		            .insert(new SingleOutcome<>(Long.class))
	        );
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
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
		if(item.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("Cet ordre de paiement ne peut être supprimé !");
		}
		if(!this.has(item.id())) {
			return;
		}
		final ReferenceDocument document = item.document();
		document.sendToTreatment();
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_payment_order",
                        "WHERE id=? AND beneficiary_id=? AND (?='NONE' OR status_id=?)"
                    ).toString()
                )
                .set(item.id())
                .set(this.tp.id())
                .set(this.status.name())
                .set(this.status.name())
                .execute()
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_payment_order_group",
                        "WHERE id NOT IN (SELECT group_id FROM pay_payment_order)"
                    ).toString()
                )
                .execute();;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE id=? AND beneficiary_id=? AND (?='NONE' OR status_id=?)")
				.set(id)
				.set(this.tp.id())
				.set(this.status.name())
				.set(this.status.name())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(final String reference) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE reference=? AND beneficiary_id=? AND (?='NONE' OR status_id=?)")
				.set(reference)
				.set(this.tp.id())
				.set(this.status.name())
				.set(this.status.name())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public PaymentOrder get(final String reference) {
		if(this.has(reference)) {
			try {
				return new DbPaymentOrder(
					this.source,
					new JdbcSession(this.source)
						.sql("SELECT id FROM pay_payment_order WHERE reference=? AND beneficiary_id=? AND (?='NONE' OR status_id=?)")
						.set(reference)
						.set(this.tp.id())
						.set(this.status.name())
						.set(this.status.name())
						.select(new SingleOutcome<>(Long.class))
				);
			} catch(SQLException ex) {
				throw new DatabaseException(ex);
			}
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement avec la référence %s n'a pas été trouvé !", reference));
		}
	}

	private PaymentOrder last() {
		try {
			final boolean hasany = new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order")
				.select(new SingleOutcome<>(Long.class)) > 0;
			if(hasany) {
				return new DbPaymentOrder(
					this.source,
					new JdbcSession(this.source)
						.sql("SELECT id FROM pay_payment_order order by id DESC limit 1")
						.select(new SingleOutcome<>(Long.class))
				);
			} else {
				return PaymentOrder.EMPTY;
			}
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	private String generateReference() {
		final Company company = new PropCompany();
		final PaymentOrder last = this.last();
		final LocalDate today = LocalDate.now();
		final LocalDate date;
		final Long nextnumber;
		if(last == PaymentOrder.EMPTY) {
			date = today;
			nextnumber = 1L;
		} else {
			final LocalDate lastdate = last.date();
			if(lastdate.getYear() != today.getYear()) {
				date = today;
				nextnumber = 1L;
			} else {
				date = lastdate;
				nextnumber = Long.parseLong(company.parameter("paymentordercurrnum")) + 1;
			}
		}
		company.parameter("paymentordercurrnum", nextnumber.toString());
		return String.format(
			"PO/%s/%s",
			date.getYear(),
			StringUtils.leftPad(Long.toString(nextnumber), 4, "0")
		);
	}

	@Override
	public Long count() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order WHERE beneficiary_id=? AND (?='NONE' OR status_id=?)")
				.set(this.tp.id())
				.set(this.status.name())
				.set(this.status.name())
				.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double totalAmount() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT SUM(amount) FROM pay_payment_order WHERE beneficiary_id=? AND (?='NONE' OR status_id=?)")
				.set(this.tp.id())
				.set(this.status.name())
				.set(this.status.name())
				.select(new SingleOutcome<>(Long.class)).doubleValue();
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
