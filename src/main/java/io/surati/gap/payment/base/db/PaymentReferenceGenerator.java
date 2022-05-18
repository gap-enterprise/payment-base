package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.Company;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.ReferenceGenerator;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public final class PaymentReferenceGenerator implements ReferenceGenerator {

    private final DataSource src;

    public PaymentReferenceGenerator(final DataSource src) {
        this.src = src;
    }

    @Override
    public String next() {
        synchronized (PaymentReferenceGenerator.class) {
            final Company company = new PropCompany();
            final Payment last = this.last();
            final LocalDate today = LocalDate.now();
            final LocalDate date;
            final Long nextnumber;
            if(last == Payment.EMPTY) {
                date = today;
                nextnumber = 1L;
            } else {
                final LocalDate lastdate = last.date();
                if(lastdate.getYear() != today.getYear()) {
                    date = today;
                    nextnumber = 1L;
                } else {
                    date = lastdate;
                    nextnumber = Long.parseLong(company.parameter("paymentcurrnum")) + 1;
                }
            }
            company.parameter("paymentcurrnum", nextnumber.toString());
            return String.format(
                "PAY/%s/%s",
                date.getYear(),
                StringUtils.leftPad(Long.toString(nextnumber), 4, "0")
            );
        }
    }

    /**
     * Get last payment.
     *
     * @return Payment
     */
    private Payment last() {
        try {
            final boolean hasany = new JdbcSession(this.src)
                .sql("SELECT COUNT(*) FROM pay_payment")
                .select(new SingleOutcome<>(Long.class)) > 0;
            if(hasany) {
                return new DbPayment(
                    this.src,
                    new JdbcSession(this.src)
                        .sql("SELECT id FROM pay_payment order by id DESC limit 1")
                        .select(new SingleOutcome<>(Long.class))
                );
            } else {
                return Payment.EMPTY;
            }
        } catch(SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
