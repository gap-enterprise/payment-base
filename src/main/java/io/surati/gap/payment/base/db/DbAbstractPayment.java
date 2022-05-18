package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.admin.base.db.DbUser;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.commons.utils.amount.XofAmountInLetters;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.PaymentCancelReason;
import io.surati.gap.payment.base.api.PaymentExport;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.PaymentStatus;
import io.surati.gap.payment.base.api.ThirdParty;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

public abstract class DbAbstractPayment implements Payment {

	/**
	 * Data source.
	 */
	protected final DataSource source;

	/**
	 * Identifier.
	 */
	protected final Long id;

	private final PaymentExport export;

	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbAbstractPayment(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
		this.export = new DbPaymentExport(this.source);
	}
	
	@Override
	public Long id() {
		return this.id;
	}

	@Override
	public String issuerReference() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT issuer_reference FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public LocalDate date() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT date FROM pay_payment",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getTimestamp(1).toLocalDateTime().toLocalDate();
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public ThirdParty beneficiary() {
		try {
			return new DbThirdParty(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT beneficiary_id FROM pay_payment",
	        				"WHERE id=?"
	        			).toString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(Long.class))
			);
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public ThirdParty issuer() {
		try {
			return new DbThirdParty(
				this.source,
				new JdbcSession(this.source)
					.sql(
						new Joined(
							" ",
							"SELECT issuer_id FROM pay_payment",
							"WHERE id=?"
						).toString()
					)
					.set(this.id)
					.select(new SingleOutcome<>(Long.class))
			);
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double amount() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT amount FROM pay_payment WHERE id=?")
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public PaymentStatus status() {
		try {
			return PaymentStatus.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT status_id FROM pay_payment",
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
	public String mention1() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT mention_1 FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String mention2() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT mention_2 FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, final boolean sendbackinpayment, final User author) {
		if(author == User.EMPTY) {
			throw new IllegalArgumentException("Vous devez renseigner l'auteur du rejet !");
		}
		if(reason == PaymentCancelReason.NONE) {
			throw new IllegalArgumentException("Vous devez indiquer une raison");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment",
                        "SET status_id=?, cancel_reason_id=?, cancel_reason_description=?, cancel_author_id=?, cancel_date=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(PaymentStatus.CANCELLED.name())
                .set(reason.name())
                .set(description)
                .set(author.id())
                .set(canceldate)
                .set(this.id)
                .execute();
            this.cancelExecution(author, sendbackinpayment);
            export.add(this);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public PaymentCancelReason reasonOfCancel() {
		try {
			final String reasonid = new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT cancel_reason_id FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
			if(StringUtils.isBlank(reasonid)) {
				return PaymentCancelReason.NONE;
			} else {
				return PaymentCancelReason.valueOf(reasonid);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String place() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT edition_place FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public User author() {
		try {
			return new DbUser(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT author_id FROM pay_payment",
	        				"WHERE id=?"
	        			).toString()
	        		)
					.set(this.id)
		            .select(new SingleOutcome<>(Long.class))
			);
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String amountInLetters() {
		return new XofAmountInLetters(this.amount()).toString();
	}

	@Override
	public String amountInHuman() {
		return new FrAmountInXof(this.amount()).toString();
	}

	@Override
	public void complete() {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(PaymentStatus.ISSUED.name())
                .set(this.id)
                .update(Outcome.VOID);
            export.add(this);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public String internalReference() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT internal_reference FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Iterable<PaymentOrder> orders() {
		try {
			return
				new JdbcSession(this.source)
					.sql(
						new Joined(
							" ",
							"SELECT id FROM pay_payment_order",
							"WHERE payment_id=?"
						).toString()
					)
					.set(this.id)
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
	public String descriptionOfCancel() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT cancel_reason_description FROM pay_payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public User authorOfCancel() {
		try {
			final Long userid = new JdbcSession(this.source)
					.sql(
			        		new Joined(
		        				" ",
		        				"SELECT cancel_author_id FROM pay_payment",
		        				"WHERE id=?"
		        			).toString()
		        		)
						.set(this.id)
			            .select(new SingleOutcome<>(Long.class));
			if(userid.equals(0L)) {
				return User.EMPTY;
			} else {
				return new DbUser(
					this.source,
					userid
				);
			}
			
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public PaymentMeanType meanType() {
		try {
			return PaymentMeanType.valueOf(
				new JdbcSession(this.source)
					.sql(
						new Joined(
							" ",
							"SELECT mean_type_id FROM pay_payment",
							"WHERE id=?"
						).toString()
					)
					.set(this.id)
					.select(new SingleOutcome<>(String.class))
			);
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	@Override
	public LocalDateTime cancelDate() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT cancel_date FROM pay_payment",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				if(rs.getTimestamp(1) == null) {
					return LocalDateTime.MIN;
				} else {
					return rs.getTimestamp(1).toLocalDateTime();
				}
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	private void cancelPaymentGroup(final PaymentOrderGroup group) {
		if (group == PaymentOrderGroup.EMPTY || group.status() == PaymentOrderStatus.CANCELLED) {
			return;
		}
		try {
			new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"UPDATE pay_payment_order_group",
						"SET status_id=?",
						"WHERE id=?"
					).toString()
				)
				.set(PaymentOrderStatus.CANCELLED)
				.set(group.id())
				.execute();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	private void cancelExecution(final User author, final boolean sentbackinpayment) {
		/*for (PaymentOrder order : this.orders()) {
			order.cancelExecution();
		}

		this.cancelPaymentGroup(order.);
		this.changeStatus(PaymentOrderStatus.CANCELLED);
		if(sentbackinpayment) {
			final Collection<PaymentOrder> duplicatedorders = new LinkedList<>();
			for (PaymentOrder order : this.iterate()) {
				duplicatedorders.add(
					order.duplicate(author)
				);
			}
			final PaymentOrderGroupsToPrepare groups = new DbPaymentOrderGroupsToPrepare(this.source, author);
			final PaymentOrderGroup newgroup;
			if(this.isHetero()) {
				newgroup = groups.mergeAcross(this.beneficiary(), duplicatedorders);
			} else {
				newgroup = groups.merge(duplicatedorders);
			}
			newgroup.useAccount(this.accountToUse());
			newgroup.update(this.meanType(), this.dueDate());
			newgroup.validate(author);
		}*/
	}
}
