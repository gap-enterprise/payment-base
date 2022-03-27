package io.surati.gap.payment.base.wp;

import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;

import java.time.LocalDate;

public abstract class BankNoteWrapper extends PaymentWrapper implements BankNote {

	private final BankNote origin;
	
	public BankNoteWrapper(final BankNote origin) {
		super(origin);
		this.origin = origin;
	}

	@Override
	public BankNoteBook book() {
		return this.origin.book();
	}

	@Override
	public LocalDate dueDate() {
		return this.origin.dueDate();
	}

}
