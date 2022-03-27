package io.surati.gap.payment.base.db;

import javax.sql.DataSource;

public final class DbPromissoryNote extends DbAbstractBankNote {

	public DbPromissoryNote(DataSource source, Long id) {
		super(source, id);

	}

}
