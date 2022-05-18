package io.surati.gap.payment.base.db;

import com.baudoliver7.fr.FrIntegerInLetters;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.Company;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNotePen;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.PaymentStatus;
import io.surati.gap.payment.base.api.ThirdParty;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.cactoos.text.Joined;

public final class DbAnonymousPaymentPen {

	private final DataSource source;

	private final PaymentOrder po;

	private final User author;

	public DbAnonymousPaymentPen(
		final DataSource source,
		final PaymentOrder po,
		final User author
	) {
		this.source = source;
		this.po = po;
		this.author = author;
	}

	public Payment write() {
		final Double amount = this.po.amountToPay();
		if(amount <= 0) {
			throw new IllegalArgumentException(
				String.format(
					"Le montant à payé doit être supérieur à 0 ! Il est actuellement de %s.",
					new FrIntegerInLetters(amount.longValue())
				)
			);
		}
		try {
			final String reference = new PaymentReferenceGenerator(this.source).next();
			final Long payid = new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"INSERT INTO pay_payment",
						"(internal_reference, issuer_reference, beneficiary_id, amount, date, edition_place, mention_1, mention_2, status_id, author_id, issuer_id, mean_type_id)",
						"VALUES",
						"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
					).toString()
				)
				.set(reference)
				.set(reference)
				.set(this.po.beneficiary().id())
				.set(amount)
				.set(java.sql.Timestamp.valueOf(LocalDateTime.now()))
				.set(new PropCompany().city())
				.set("")
				.set("")
				.set(PaymentStatus.ISSUED.name())
				.set(this.author.id())
				.set(2L)
				.set(PaymentMeanType.ANONYMOUS.name())
				.insert(new SingleOutcome<>(Long.class));
			new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"UPDATE pay_payment_order",
						"SET payment_id=?, status_id=?",
						"WHERE id=?"
					).toString()
				)
				.set(payid)
				.set(PaymentOrderStatus.EXECUTED.name())
				.set(this.po.id())
				.execute();
	        return new DbPayment(this.source, payid);
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
	}
}
