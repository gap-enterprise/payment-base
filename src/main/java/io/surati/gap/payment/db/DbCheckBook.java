package io.surati.gap.payment.db;

import javax.sql.DataSource;

public final class DbCheckBook extends DbAbstractBankNoteBook {

	public DbCheckBook(DataSource source, Long id) {
		super(source, id);
	}
}
