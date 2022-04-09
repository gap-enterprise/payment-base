package io.surati.gap.payment.base.db;

import io.surati.gap.payment.base.api.Payment;
import javax.sql.DataSource;

public final class DbPayment extends DbAbstractPayment implements Payment {

	/**
	 * Ctor.
	 *
	 * @param source Data source
	 * @param id     Identifier
	 */
	public DbPayment(final DataSource source, final Long id) {
		super(source, id);
	}

	@Override
	public String name() {
		return String.format(
			"%s N°%s", this.meanType().toString(), this.issuerReference()
		);
	}
}
