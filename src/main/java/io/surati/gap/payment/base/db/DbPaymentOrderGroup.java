package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.ThirdParty;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

public final class DbPaymentOrderGroup implements PaymentOrderGroup {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	private final Long id;

	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbPaymentOrderGroup(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}

	@Override
	public Iterable<PaymentOrder> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT pol.id FROM pay_payment_order_group_line pol",
							"LEFT JOIN pay_payment_order po ON po.id = pol.id",
                            "WHERE pol.group_id=?",
            				"ORDER BY po.date ASC"
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
	public PaymentOrder get(Long id) {
		if(this.has(id)) {
			return new DbPaymentOrder(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement (ID=%s) n'a pas été trouvé !", id));
		}
	}

	private boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order_group_line WHERE id=? AND group_id=?")
				.set(id)
				.set(this.id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(final PaymentOrder item) {
		return this.has(item.id());
	}

	@Override
	public Long count() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_payment_order_group_line WHERE group_id=?")
				.set(this.id)
				.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double totalAmount() {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT SUM(po.amount) FROM pay_payment_order po LEFT JOIN pay_payment_order_group_line pol ON pol.id=po.id WHERE pol.group_id=?")
				.set(this.id)
				.select(new SingleOutcome<>(Long.class)).doubleValue();
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccount accountToUse() {
		try {
			final Long accountid = new JdbcSession(this.source)
				.sql(
	        		new Joined(
	    				" ",
	    				"SELECT account_id FROM pay_payment_order_group",
	    				"WHERE id=?"
	    			).toString()
	    		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class));
			if(accountid.equals(0L)) {
				return BankAccount.EMPTY;
			} else {
				return new DbBankAccount(this.source, accountid);
			}
		} catch(SQLException e) {
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
	        				"SELECT third_party_id FROM pay_payment_order_group",
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
	public void useAccount(BankAccount account) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order_group",
                        "SET account_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(account == BankAccount.EMPTY ? null : account.id())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void validate(final User author) {
		if(this.totalAmount() <= 0) {
			throw new IllegalArgumentException(
				String.format(
					"Le montant du groupe (Bénéficiaire=%s, Montant=%s) doit être supérieur à 0 !",
					this.beneficiary().abbreviated(),
					new FrAmountInXof(this.totalAmount())
				)
			);
		}
		if(this.accountToUse() == BankAccount.EMPTY) {
			throw new IllegalArgumentException(
				String.format(
					"Le compte bancaire du groupe (Bénéficiaire=%s, Montant=%s) doit être spécifié !",
					this.beneficiary().abbreviated(),
					new FrAmountInXof(this.totalAmount())
				)
			);
		}
		if(this.meanType() == PaymentMeanType.NONE) {
			throw new IllegalArgumentException(
				String.format(
					"Le moyen de paiement du groupe (Bénéficiaire=%s, Montant=%s) doit être spécifié !",
					this.beneficiary().abbreviated(),
					new FrAmountInXof(this.totalAmount())
				)
			);
		}
		if(this.meanType() == PaymentMeanType.LC && this.dueDate() == LocalDate.MIN) {
			throw new IllegalArgumentException(
				String.format(
					"La date d'échéance pour le groupe (Bénéficiaire=%s, Montant=%s) doit être spécifié !",
					this.beneficiary().abbreviated(),
					new FrAmountInXof(this.totalAmount())
				)
			);
		}
		for (PaymentOrder order : this.iterate()) {
			order.validate(author);
		}
		this.changeStatus(PaymentOrderStatus.IN_WAITING_FOR_PAYMENT);
	}

	@Override
	public void sendBackInPreparation() {
		for (PaymentOrder order : this.iterate()) {
			order.sendBackInPreparation();
		}
		this.changeStatus(PaymentOrderStatus.TO_PREPARE);
	}
	
	private void changeStatus(final PaymentOrderStatus status) {
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
                .set(status.name())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public PaymentOrderStatus status() {
		try {
			return PaymentOrderStatus.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT status_id FROM pay_payment_order_group",
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
	public Long id() {
		return this.id;
	}

	@Override
	public void execute() {
		for (PaymentOrder order : this.iterate()) {
			order.execute();
		}
		this.changeStatus(PaymentOrderStatus.EXECUTED);
	}

	@Override
	public void cancelExecution(final User author, final boolean sentbackinpayment) {
		for (PaymentOrder order : this.iterate()) {
			order.cancelExecution();
		}
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
		}
	}
	

	@Override
	public PaymentMeanType meanType() {
		try {
			final String meantypeid = new JdbcSession(this.source)
					.sql(
			        		new Joined(
		        				" ",
		        				"SELECT mean_type_id FROM pay_payment_order_group",
		        				"WHERE id=?"
		        			).toString()
		        		)
						.set(this.id)
			            .select(new SingleOutcome<>(String.class));
			if(StringUtils.isBlank(meantypeid)) {
				return PaymentMeanType.NONE;
			} else {
				return PaymentMeanType.valueOf(meantypeid);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public LocalDate dueDate() {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement(
					new Joined(
	    				" ",
	    				"SELECT due_date FROM pay_payment_order_group",
	    				"WHERE id=?"
	    			).toString()
				)
			){
				pstmt.setLong(1, id);
				try (final ResultSet rs = pstmt.executeQuery()) {
					rs.next();
					if(rs.getDate(1) == null) {
						return LocalDate.MIN;
					} else {
						return rs.getDate(1).toLocalDate();
					}
				}
			} catch (SQLException ex) {
				throw new DatabaseException(ex);
			}
	}

	@Override
	public void update(PaymentMeanType meantype, LocalDate duedate) {
		try {
			final LocalDate lduedate;
			if(duedate == null || meantype == PaymentMeanType.NONE || meantype == PaymentMeanType.CHQ) {
				lduedate = null;
			} else {
				lduedate = duedate;
			}
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order_group",
                        "SET mean_type_id=?, due_date=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(meantype.name())
                .set(lduedate == null ? null : java.sql.Date.valueOf(lduedate))
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public boolean isHetero() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT is_hetero FROM pay_payment_order_group",
        				"WHERE id=?"
        			).toString()
				)
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getBoolean(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void changeBeneficiary(final ThirdParty tp) {
		if (this.beneficiary().equals(tp)) {
			return;
		}
		try {
			final boolean ishetero = new JdbcSession(this.source)
	            .sql(
	                new Joined(
	                    " ",
	                    "SELECT COUNT(*) FROM pay_payment_order",
	                    "WHERE id IN (SELECT id FROM pay_payment_order_group_line WHERE group_id=?) AND beneficiary_id=?"
	                ).toString()
	            )
	            .set(this.id)
	            .set(tp.id())
	            .select(new SingleOutcome<>(Long.class))
	            .equals(0L);
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order_group",
                        "SET third_party_id=?, is_hetero=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(tp.id())
                .set(ishetero)
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
}
