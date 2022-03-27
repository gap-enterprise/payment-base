package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.PaymentMean;
import io.surati.gap.payment.api.PaymentMeanField;
import io.surati.gap.payment.api.PaymentMeanFieldType;
import io.surati.gap.payment.api.Point;
import io.surati.gap.payment.impl.PointImpl;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DbPaymentMeanField implements PaymentMeanField {

	private final DataSource source;
	
	private final PaymentMeanFieldType type;

	private final PaymentMean mean;
	
	public DbPaymentMeanField(final DataSource source, final PaymentMeanFieldType type, final PaymentMean mean) {
		this.source = source;
		this.type = type;
		this.mean = mean;
	}

	@Override
	public PaymentMeanFieldType type() {
		return this.type;
	}

	@Override
	public PaymentMean mean() {
		return this.mean;
	}

	@Override
	public Point location() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT x, y FROM pay_payment_mean_field",
            				"WHERE type_id=? AND mean_id=?"
                        ).asString()
                    )
                    .set(this.type.name())
                    .set(this.mean.id())
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new PointImpl(
                                rset.getDouble(1),
                                rset.getDouble(2)
                            )
                        )
                    ).get(0);
        } catch (SQLException | IOException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public double width() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT width FROM pay_payment_mean_field",
        				"WHERE type_id=? AND mean_id=?"
        			).toString()
				)
		){
			pstmt.setString(1, this.type.name());
			pstmt.setLong(2, this.mean.id());
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void update(Point point, double width) {
		try (
			final Connection connection = source.getConnection();				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_payment_mean_field SET x=?, y=?, width=? WHERE type_id=? AND mean_id=?")
		) {
			pstmt.setDouble(1, point.x());
			pstmt.setDouble(2, point.y());
			pstmt.setDouble(3, width);
			pstmt.setString(4, this.type.name());
			pstmt.setLong(5, this.mean.id());
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String name() {
		return this.type.toString();
	}

	@Override
	public boolean visible() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT visible FROM pay_payment_mean_field",
        				"WHERE type_id=? AND mean_id=?"
        			).toString()
				)
		){
			pstmt.setString(1, this.type.name());
			pstmt.setLong(2, this.mean.id());
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getBoolean(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void show() {
		try (
			final Connection connection = source.getConnection();				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_payment_mean_field SET visible=? WHERE type_id=? AND mean_id=?")
		) {
			pstmt.setBoolean(1, true);
			pstmt.setString(2, this.type.name());
			pstmt.setLong(3, this.mean.id());
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void disappear() {
		try (
			final Connection connection = source.getConnection();				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE pay_payment_mean_field SET visible=? WHERE type_id=? AND mean_id=?")
		) {
			pstmt.setBoolean(1, false);
			pstmt.setString(2, this.type.name());
			pstmt.setLong(3, this.mean.id());
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

}
