package io.surati.gap.payment.db;

import javax.sql.DataSource;

public final class DbBillOfExchangeBook extends DbAbstractBankNoteBook {

	public DbBillOfExchangeBook(DataSource source, Long id) {
		super(source, id);
	}
}
