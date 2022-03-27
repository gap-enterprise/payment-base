package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.PaymentBatch;
import io.surati.gap.payment.api.PaymentBatches;
import io.surati.gap.payment.api.PaymentMeanType;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class DbPaymentBatches implements PaymentBatches {

	private final DataSource source;
	
	public DbPaymentBatches(final DataSource source) {
		this.source = source;
	}

	@Override
	public PaymentBatch get(Long id) {
		if(this.has(id)) {
			return new DbPaymentBatch(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le lot de paiement (ID=%s) n'a pas été trouvé !", id));
		}
	}

	@Override
	public PaymentBatch get(BankAccount account, PaymentMeanType meantype) {
		throw new IllegalArgumentException("DbPaymentBatche#get(account, meantype)");
	}

	@Override
	public Iterable<PaymentBatch> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM pay_payment_batch",
            				"ORDER BY date DESC"
                        ).toString()
                    )
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbPaymentBatch(
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
	public Double totalAmount() {
		throw new IllegalArgumentException("DbPaymentBatche#totalAmount");
	}

	@Override
	public PaymentBatch add(LocalDate date, BankAccount account, PaymentMeanType meantype) {
		try {
			return new DbPaymentBatch(
	            this.source,
	    		new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "INSERT INTO pay_payment_batch",
		                    "(date, account_id, mean_type_id)",
		                    "VALUES",
		                    "(?, ?, ?)"
		                ).toString()
		            )
		            .set(java.sql.Date.valueOf(date))
		            .set(account.id())
		            .set(meantype.name())
		            .insert(new SingleOutcome<>(Long.class))
	        );
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
	}

	private boolean has(final Long id) {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) as nb FROM pay_payment_batch WHERE id=?")
		){
			pstmt.setLong(1, id);
		
			try(final ResultSet rs = pstmt.executeQuery()){
				rs.next();
				Long nb = rs.getLong(1);
				return nb > 0;
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
