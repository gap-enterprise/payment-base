package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.admin.base.db.DbUser;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.PaymentOrders;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.ThirdPartyPaymentOrders;
import io.surati.gap.payment.base.api.ThirdPartyReferenceDocuments;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class DbReferenceDocument implements ReferenceDocument {

	private final DataSource source;
	
	private final Long id;
	
	public DbReferenceDocument(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}
	
	@Override
	public Long id() {
		return this.id;
	}

	@Override
	public ReferenceDocumentType type() {
		try {
			return ReferenceDocumentType.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT type_id FROM pay_reference_document",
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
	public LocalDate date() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT date FROM pay_reference_document",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDate(1).toLocalDate();
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String reference() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT reference FROM pay_reference_document",
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
	public String object() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT object FROM pay_reference_document",
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
	public String place() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT place FROM pay_reference_document",
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
	public void update(final LocalDate date, final String reference, final String object, final String place) {
		if(this.status() == ReferenceDocumentStatus.PAID) {
			throw new IllegalArgumentException("Vous ne pouvez pas modifier un document soldé !");
		}
		if(StringUtils.isBlank(reference)) {
			throw new IllegalArgumentException("Vous devez renseigner la référence du document !");
		}
		if(StringUtils.isBlank(place)) {
			throw new IllegalArgumentException("Vous devez renseigner le lieu d'édition du document !");
		}
		if(StringUtils.isBlank(object)) {
			throw new IllegalArgumentException("Vous devez renseigner l'objet du document !");
		}
		final ThirdPartyReferenceDocuments issuerdocs = new DbThirdPartyReferenceDocuments(this.source, this.issuer());
		if(
			!this.reference().equals(reference) && issuerdocs.has(reference, this.type())
		) {
			throw new IllegalArgumentException(
				String.format("La référence du document (%s N°%s) est déjà utilisée par le tiers (%s)!",
					this.type(),
					reference,
					this.issuer().abbreviated()
				)
			);
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET date=?, reference=?, object=?, place=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(java.sql.Date.valueOf(date))
                .set(reference)
                .set(object)
                .set(place)
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void update(String otherref) {
		if(this.status() == ReferenceDocumentStatus.PAID) {
			throw new IllegalArgumentException("Vous ne pouvez pas modifier un document soldé !");
		}
		if(this.contains(otherref)) {
			throw new IllegalArgumentException(
				String.format("L'autre référence du document (%s N°%s) est déjà utilisée par le tiers (%s)!",
					this.type(),
					otherref,
					this.issuer().abbreviated()
				)
			);
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET other_reference=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(otherref)
                .set(this.id)
                .execute();
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
	        				"SELECT issuer_id FROM pay_reference_document",
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
	public LocalDate depositDate() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT deposit_date FROM pay_reference_document",
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
	public LocalDate entryDate() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement(
				new Joined(
    				" ",
    				"SELECT entry_date FROM pay_reference_document",
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
	public void update(LocalDate depositdate, LocalDate entrydate) {
		if(entrydate == LocalDate.MIN) {
			throw new IllegalArgumentException("Vous devez renseigner la date de saisie !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET deposit_date=?, entry_date=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(depositdate == LocalDate.MIN ? null : java.sql.Date.valueOf(depositdate))
                .set(java.sql.Date.valueOf(entrydate))
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Double amount() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT amount FROM pay_reference_document",
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
	public void amount(Double amount, Double advamount) {
		if((amount > 0 && advamount < 0) || (amount < 0 && advamount > 0)) {
			throw new IllegalArgumentException("Le montant total et le montant avancé doivent avoir le même signe !");
		}
		if(Math.abs(amount) < Math.abs(advamount)) {
			throw new IllegalArgumentException("Le montant avancé ne peut pas être supérieur au montant total !");
		}
		if(Math.abs(amount) < Math.abs(this.amountPaid())) {
			throw new IllegalArgumentException("Le montant total doit être supérieur ou égal à la somme des montants payés !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET amount=?, advanced_amount=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(amount)
                .set(advamount)
                .set(this.id)
                .execute();
            this.updateState();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Double amountPaid() {
		try {
            return
            	new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "SELECT amount_paid",
                        "FROM pay_reference_document",
                        "WHERE id=?"
                    ).toString()
                )
                .set(this.id)
                .select(new SingleOutcome<>(Long.class))
                .doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Double amountLeft() {
		try {
            return
            	new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "SELECT amount_left",
                        "FROM pay_reference_document",
                        "WHERE id=?"
                    ).toString()
                )
                .set(this.id)
                .select(new SingleOutcome<>(Long.class))
                .doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public ReferenceDocumentStatus status() {		
		try {
			return ReferenceDocumentStatus.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
		    				" ",
		    				"SELECT status_id FROM pay_reference_document",
		    				"WHERE id=?"
		    			).toString()
		    		)
					.set(this.id)
		            .select(new SingleOutcome<>(String.class))
	        );
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public Iterable<Payment> payments() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment pay",
                            "WHERE group_id IN (",
                            "SELECT group_id FROM pay_payment_order",
                            "WHERE group_id=pay.group_id and reference_document_id=?",
                            ")",
            				"ORDER BY pay.date, id DESC"
                        ).toString()
                    )
                    .set(this.id)
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPayment(
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
	public PaymentOrder preparePayment(final User author) {
		if(this.status() == ReferenceDocumentStatus.PAID) {
			throw new IllegalArgumentException("Ce document de référence est déjà réglé !");
		}
		final ThirdPartyPaymentOrders orders = new DbThirdPartyPaymentOrders(this.source, this.issuer());
		final PaymentOrder order = orders.add(this.amountLeft(), this.object(), StringUtils.EMPTY, author);
		order.joinTo(this);
		this.changeStep(ReferenceDocumentStep.IN_PREPARATION_FOR);
		return order;
	}
	
	private boolean contains(final String otherref) {
		if(
			StringUtils.isBlank(otherref) ||
			(!StringUtils.isBlank(otherref) && otherref.equals(this.reference()))
		) {
			return false;
		}
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT COUNT(*) FROM pay_reference_document",
        				"WHERE other_reference=? AND issuer_id=? AND type_id=?"
        			).toString()
        		)
				.set(otherref)
				.set(this.issuer().id())
				.set(this.type().name())
	            .select(new SingleOutcome<>(Long.class)) > 0;
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void type(ReferenceDocumentType type) {
		if(type == ReferenceDocumentType.NONE) {
			throw new IllegalArgumentException("Vous devez renseigner un type de document !");
		}
		if(this.status() != ReferenceDocumentStatus.WAITING_FOR_PAYMENT) {
			return;
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET type_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(type.name())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void beneficiary(final ThirdParty beneficiary) {
		if(beneficiary == ThirdParty.EMPTY) {
			throw new IllegalArgumentException("Vous devez renseigner un bénéficiaire !");
		}
		if(this.status() != ReferenceDocumentStatus.WAITING_FOR_PAYMENT) {
			throw new IllegalArgumentException("Vous ne pouvez pas changer le bénéficiaire d'un document de référence déjà entamé !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET issuer_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(beneficiary.id())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public ReferenceDocumentStep step() {
		try {
			return ReferenceDocumentStep.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
		    				" ",
		    				"SELECT step_id FROM pay_reference_document",
		    				"WHERE id=?"
		    			).toString()
		    		)
					.set(this.id)
		            .select(new SingleOutcome<>(String.class))
	        );
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	private void changeStep(final ReferenceDocumentStep step) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET step_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(step.name())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void sendToTreatment() {
		this.changeStep(ReferenceDocumentStep.TO_TREAT);
	}

	@Override
	public void sendInPayment() {
		this.changeStep(ReferenceDocumentStep.IN_PAYMENT);
	}

	@Override
	public void archive() {
		this.changeStep(ReferenceDocumentStep.ARCHIVED);
	}

	@Override
	public void sendBackInPreparation() {
		this.changeStep(ReferenceDocumentStep.IN_PREPARATION_FOR);
	}
	
	@Override
	public void updateState() {
		double amountpaid = this.advancedAmount();
		final PaymentOrders orders = new DbReferenceDocumentPaymentOrders(this.source, this);
		for (PaymentOrder order : orders.iterate()) {
			if(order.status() == PaymentOrderStatus.EXECUTED) {
				amountpaid += order.amountToPay();
			}
		}
		final double amountleft = this.amount() - amountpaid;
		final ReferenceDocumentStatus status;
		if(amountleft == 0) {
			status = ReferenceDocumentStatus.PAID;
		} else if(Math.abs(amountpaid) > 0) {
			status = ReferenceDocumentStatus.PAID_PARTIALLY;
		} else {
			status = ReferenceDocumentStatus.WAITING_FOR_PAYMENT;
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_reference_document",
                        "SET status_id=?, amount_paid=?, amount_left=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(status.name())
                .set(amountpaid)
                .set(amountleft)
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public String otherReference() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT other_reference FROM pay_reference_document",
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
	        				"SELECT author_id FROM pay_reference_document",
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
	public Double advancedAmount() {
		try {
            return
            	new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "SELECT advanced_amount",
                        "FROM pay_reference_document",
                        "WHERE id=?"
                    ).toString()
                )
                .set(this.id)
                .select(new SingleOutcome<>(Long.class))
                .doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
}
