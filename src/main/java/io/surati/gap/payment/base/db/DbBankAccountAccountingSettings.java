package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankAccountAccountingSetting;
import io.surati.gap.payment.base.api.BankAccountAccountingSettings;
import io.surati.gap.payment.base.api.PaymentMeanType;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbBankAccountAccountingSettings implements BankAccountAccountingSettings {

	private final BankAccount account;

	private final DataSource source;

	public DbBankAccountAccountingSettings(final DataSource source, final BankAccount account) {
		this.source = source;
		this.account = account;
	}

	@Override
	public Iterable<BankAccountAccountingSetting> iterate() {
		try {
			return new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "SELECT mean_type_id",
                        "FROM pay_bank_account_accounting_setting",
                        "WHERE account_id=?",
        				"ORDER BY mean_type_id ASC"
                    ).toString()
                )
                .set(this.account.id())
                .select(
					new ListOutcome<>(
						rset ->
							new DbBankAccountAccountingSetting(
								this.source,
								this.account,
								PaymentMeanType.valueOf(rset.getString(1))
							)
					)
                );
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public BankAccountAccountingSetting get(final PaymentMeanType meantype) {
		if(this.has(meantype)) {
			return new DbBankAccountAccountingSetting(this.source, this.account, meantype);
		} else {
			throw new IllegalArgumentException(String.format("Le paramètre comptable du compte %s n'a pas été trouvé !", this.account.rib()));
		}
	}

	private boolean has(final PaymentMeanType meantype) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM pay_bank_account_accounting_setting WHERE account_id=? AND mean_type_id=?")
				.set(this.account.id())
				.set(meantype.name())
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
