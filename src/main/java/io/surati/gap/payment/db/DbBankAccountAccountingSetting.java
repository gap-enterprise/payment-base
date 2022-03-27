package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.BankAccountAccountingSetting;
import io.surati.gap.payment.api.PaymentMeanType;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbBankAccountAccountingSetting implements BankAccountAccountingSetting {

	private final DataSource source;

	private final BankAccount account;
	
	private final PaymentMeanType meantype;

	public DbBankAccountAccountingSetting(final DataSource source, final BankAccount account, final PaymentMeanType meantype) {
		this.source = source;
		this.account = account;
		this.meantype = meantype;
	}

	@Override
	public PaymentMeanType meanType() {
		return this.meantype;
	}

	@Override
	public String journalCode() {
		try {
			return new JdbcSession(this.source)
				.sql(
					new Joined(
						" ",
						"SELECT journal_code",
						"FROM pay_bank_account_accounting_setting",
						"WHERE mean_type_id=? AND account_id=?"
					).toString()
				)
				.set(meantype.name())
				.set(account.id())
				.select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccount account() {
		return this.account;
	}

	@Override
	public void update(String journalcode) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE pay_bank_account_accounting_setting",
                        "SET journal_code=?",
                        "WHERE mean_type_id=? AND account_id=?"
                    ).toString()
                )
                .set(journalcode)
                .set(this.meantype.name())
				.set(this.account.id())
                .update(Outcome.VOID);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

}
