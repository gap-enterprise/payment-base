package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.PaymentBatch;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentStatus;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.list.ListOf;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public final class DbPaymentBatch implements PaymentBatch {

	private final DataSource source;
	
	private final Long id;

	public DbPaymentBatch(final DataSource source, final Long id) {
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
    				"SELECT date FROM payment_batch",
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
	public BankAccount account() {
		try {
			return new DbBankAccount(
				this.source,
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
		    				" ",
		    				"SELECT account_id FROM payment_batch",
		    				"WHERE id=?"
		    			).toString()
		    		)
					.set(this.id)
		            .select(new SingleOutcome<>(Long.class))
			 );
		} catch(SQLException e) {
			throw new DatabaseException(e);
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
	        				"SELECT mean_type_id FROM payment_batch",
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
	public Iterable<PaymentOrderGroup> groups() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT group_id FROM payment",
                            "WHERE batch_id=?",
            				"ORDER BY date ASC"
                        ).toString()
                    )
                    .set(this.id)
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
	public Iterable<BankNote> notes() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM payment",
                            "WHERE batch_id=?",
            				"ORDER BY id ASC"
                        ).toString()
                    )
                    .set(this.id)
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNote(
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
	public void add(PaymentOrderGroup group) {
		throw new UnsupportedOperationException("DbPaymentBatch#add");
	}

	@Override
	public BankNote pay(PaymentOrderGroup group, Iterable<BankNoteBook> books, User author) {
		final List<BankNote> notes = new ListOf<>(this.notes());
		final Iterator<BankNoteBook> it = books.iterator();
		BankNoteBook book = it.next();
		if(!notes.isEmpty()) {
			final String precnumber = notes.get(notes.size() - 1).issuerReference();
			if(!book.hasNextNoteAfter(precnumber)) {
				book = it.next();
			}
		}
		final BankNote item = new DbBankNotePen(
			this.source,
			book,
			group,
			this.date(),
			StringUtils.EMPTY,
			StringUtils.EMPTY,
			group.dueDate(),
			author
		).write();
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE payment",
                        "SET batch_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(this.id)
                .set(item.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
		return item;
	}

	@Override
	public PaymentStatus status() {
		try {
			return PaymentStatus.valueOf(
				new JdbcSession(this.source)
					.sql(
		        		new Joined(
	        				" ",
	        				"SELECT status_id FROM payment_batch",
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
	public void terminate() {
		for (BankNote note : this.notes()) {
			if(note.status() != PaymentStatus.TO_PRINT) {
				continue;
			}
			note.complete();
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE payment_batch",
                        "SET status_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(PaymentStatus.ISSUED.name())
                .set(this.id)
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

}
