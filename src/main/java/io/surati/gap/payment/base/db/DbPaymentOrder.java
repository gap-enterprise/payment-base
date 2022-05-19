package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.admin.base.db.DbUser;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.ThirdPartyPaymentOrders;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class DbPaymentOrder implements PaymentOrder {

	private final DataSource source;
	
	private final Long id;
	
	public DbPaymentOrder(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}
	
	@Override
	public Long id() {
		return this.id;
	}
	
	@Override
	public LocalDate date() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT date FROM pay_payment_order",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDate(1).toLocalDate();
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public String reference() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT reference FROM pay_payment_order",
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
	public ThirdParty beneficiary() {
		try {
			return new DbThirdParty(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT beneficiary_id FROM pay_payment_order",
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
	public Double amountToPay() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT amount FROM pay_payment_order",
        				"WHERE id=?"
        			).toString()
				)
		){
			pstmt.setLong(1, this.id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String amountToPayInHuman() {
		return new FrAmountInXof(this.amountToPay()).toString();
	}

	@Override
	public ReferenceDocument document() {
		try {
			final Long id = new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT reference_document_id FROM pay_payment_order",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class));
			final ReferenceDocument item;
			if(id.equals(0L)) {
				item = ReferenceDocument.EMPTY;
			} else {
				item = new DbReferenceDocument(this.source, id);
			}
			return item;
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void update(LocalDate date, ThirdParty beneficiary, double amounttopay, String reason, String description) {
		if(StringUtils.isBlank(reason)) {
			throw new IllegalArgumentException("Vous devez donner un motif de règlement !");
		}
		if(this.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("Cet ordre de paiement ne peut être modifé !");
		}
		final ReferenceDocument document = document();
		if(document != ReferenceDocument.EMPTY && Math.abs(document.amountLeft()) < Math.abs(amounttopay)) {
			throw new IllegalArgumentException("Le montant à régler dépasse le montant restant du document de référence !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
                        "SET date=?, beneficiary_id=?, amount=?, reason=?, description=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(java.sql.Date.valueOf(date))
                .set(beneficiary.id())
                .set(amounttopay)
                .set(reason)
                .set(description)
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public User authorizingOfficer() {
		try {
			final Long userid = new JdbcSession(this.source)
			   .sql(
	        		new Joined(
        				" ",
        				"SELECT authorizing_officer_id FROM pay_payment_order",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class));
			final User user;
			if(userid.equals(0L)) {
				user = User.EMPTY;
			} else {
				user = new DbUser(this.source, userid);
			}
			return user;
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
	        				"SELECT status_id FROM pay_payment_order",
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
	public void validate(final User author) {
		if(this.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("L'ordre de paiement ne peut pas être validé !");
		}
		final ReferenceDocument doc = this.document();
		if(doc.status() == ReferenceDocumentStatus.PAID) {
			throw new IllegalArgumentException("Le document de référence est déjà payé !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
                        "SET status_id=?, authorizing_officer_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(PaymentOrderStatus.IN_WAITING_FOR_PAYMENT.name())
                .set(author.id())
                .set(this.id)
                .execute();
            doc.sendInPayment();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void execute() {
		if(this.status() != PaymentOrderStatus.IN_WAITING_FOR_PAYMENT) {
			throw new IllegalArgumentException("L'ordre de paiement ne peut pas être exécuté !");
		}
		final ReferenceDocument doc = this.document();
		if(doc.status() == ReferenceDocumentStatus.PAID) {
			throw new IllegalArgumentException("Le document de référence est déjà payé !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(PaymentOrderStatus.EXECUTED.name())
                .set(this.id)
                .execute();
            doc.updateState();
            if(doc.status() == ReferenceDocumentStatus.PAID) {
            	doc.archive();
            } else {
            	doc.sendToTreatment();
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void cancelExecution() {
		this.changeStatus(PaymentOrderStatus.CANCELLED);
		final ReferenceDocument document = this.document();
		if(document != ReferenceDocument.EMPTY && document.step() != ReferenceDocumentStep.TO_TREAT) {
			document.sendToTreatment();
			document.updateState();
		}
	}
	
	@Override
	public void assign(final BankAccount account) {
		if(this.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("Cet ordre de paiement ne peut être modifé !");
		}
		try (
			final Connection connection = source.getConnection();
				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_payment_order SET bank_account_id=? WHERE id=?")
		) {
			if(account == BankAccount.EMPTY) {
				pstmt.setObject(1, null);
			} else {
				pstmt.setLong(1, account.id());
			}
			
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void assign(final PaymentMeanType meanType) {
		if(this.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("Cet ordre de paiement ne peut être modifé !");
		}
		try {
			new JdbcSession(this.source)
            .sql(
                new Joined(
                    " ",
                    "UPDATE pay_payment_order",
                    "SET mean_type_id=?",
                    "WHERE id=?"
                ).asString()
            )
            .set(meanType.name())
            .set(this.id)
            .insert(Outcome.VOID);
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
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
	        				"SELECT author_id FROM pay_payment_order",
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
	public void joinTo(final ReferenceDocument document) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
                        "SET reference_document_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(document.id())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void sendBackInPreparation() {
		if(this.status() != PaymentOrderStatus.IN_WAITING_FOR_PAYMENT) {
			throw new IllegalArgumentException("L'ordre de paiement ne peut être ramené en préparation !");
		}
		final ReferenceDocument doc = this.document();
		doc.sendBackInPreparation();
		this.changeStatus(PaymentOrderStatus.TO_PREPARE);
	}
	
	private void changeStatus(final PaymentOrderStatus status) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
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
	public PaymentOrder duplicate(final User author) {
		final ThirdPartyPaymentOrders orders = new DbThirdPartyPaymentOrders(this.source, this.beneficiary());
		final PaymentOrder order = orders.add(this.amountToPay(), this.reason(), this.description(), author);
		if(this.document() != ReferenceDocument.EMPTY) {
			order.joinTo(this.document());
		}
		return order;
	}

	@Override
	public String reason() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT reason FROM pay_payment_order",
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
	public String description() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT description FROM pay_payment_order",
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
	public PaymentOrder split(final double firstamount, final User author) {
		if(firstamount == 0) {
			throw new IllegalArgumentException("Vous devez spécifier un montant !");
		}
		final double beginamount = this.amountToPay();
		if(firstamount < 0 && beginamount > 0) {
			throw new IllegalArgumentException("Le montant doit être positif !");
		}
		if(firstamount >= beginamount && beginamount > 0) {
			throw new IllegalArgumentException("Le montant doit être inférieur au montant de départ !");
		}
		if(firstamount > 0 && beginamount < 0) {
			throw new IllegalArgumentException("Le montant doit être négatif !");
		}
		if(firstamount <= beginamount && beginamount < 0) {
			throw new IllegalArgumentException("Le montant doit être compris entre 0 et le montant de départ !");
		}
		final PaymentOrder secondorder = this.duplicate(author);
		final double secondamount = this.amountToPay() - firstamount;
		this.amount(firstamount);
		secondorder.amount(secondamount);
		return secondorder;
	}

	@Override
	public void amount(double newamount) {
		if(this.status() != PaymentOrderStatus.TO_PREPARE) {
			throw new IllegalArgumentException("Cet ordre de paiement ne peut être modifé !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_payment_order",
                        "SET amount=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(newamount)
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
}
