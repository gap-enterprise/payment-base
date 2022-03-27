package io.surati.gap.payment.base.db;

import javax.sql.DataSource;

public final class DbCheck extends DbAbstractBankNote {

	public DbCheck(DataSource source, Long id) {
		super(source, id);
	}

}
