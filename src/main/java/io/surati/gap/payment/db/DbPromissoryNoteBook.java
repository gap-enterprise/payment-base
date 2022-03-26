package io.surati.gap.payment.db;

import javax.sql.DataSource;

public final class DbPromissoryNoteBook extends DbAbstractBankNoteBook {

	public DbPromissoryNoteBook(DataSource source, Long id) {
		super(source, id);
	}
}
