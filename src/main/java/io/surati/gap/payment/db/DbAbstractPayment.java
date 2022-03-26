package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.User;
import io.surati.gap.admin.db.DbUser;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.commons.utils.amount.XofAmountInLetters;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Payment;
import io.surati.gap.payment.api.PaymentCancelReason;
import io.surati.gap.payment.api.PaymentExport;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentStatus;
import io.surati.gap.payment.api.ThirdParty;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        				"SELECT issuer_reference FROM payment",
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
    				"SELECT date FROM payment",
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
	public ThirdParty beneficiary() {
		try {
			return new DbThirdParty(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT beneficiary_id FROM payment",
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
							"SELECT issuer_id FROM payment",
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
			final PreparedStatement pstmt = connection.prepareStatement("SELECT amount FROM payment WHERE id=?")
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
	        				"SELECT status_id FROM payment",
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
        				"SELECT mention_1 FROM payment",
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
        				"SELECT mention_2 FROM payment",
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
                        "UPDATE payment",
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
            this.orders().cancelExecution(author, sendbackinpayment);
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
        				"SELECT cancel_reason_id FROM payment",
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
        				"SELECT edition_place FROM payment",
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
	        				"SELECT author_id FROM payment",
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
                        "UPDATE payment",
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
        				"SELECT internal_reference FROM payment",
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
	public PaymentOrderGroup orders() {
		try {
			final Long groupid = new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT group_id FROM payment",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.id)
	            .select(new SingleOutcome<>(Long.class));
			if(groupid.equals(0L)) {
				return PaymentOrderGroup.EMPTY;
			} else {
				return new DbPaymentOrderGroup(this.source, groupid);
			}
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
        				"SELECT cancel_reason_description FROM payment",
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
		        				"SELECT cancel_author_id FROM payment",
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
							"SELECT mean_type_id FROM payment",
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
    				"SELECT cancel_date FROM payment",
    				"WHERE id=?"
    			).toString()
			)
		){
			pstmt.setLong(1, id);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				if(rs.getDate(1) == null) {
					return null;
				} else {
					return rs.getTimestamp(1).toLocalDateTime();
				}
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
