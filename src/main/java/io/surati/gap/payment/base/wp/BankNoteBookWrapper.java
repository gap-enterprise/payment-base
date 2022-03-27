package io.surati.gap.payment.base.wp;

import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.PaymentMeanType;

public abstract class BankNoteBookWrapper implements BankNoteBook {

	private final BankNoteBook origin;
	
	public BankNoteBookWrapper(final BankNoteBook origin) {
		this.origin = origin;
	}

	@Override
	public Long id() {
		return this.origin.id();
	}

	@Override
	public BankAccount account() {
		return this.origin.account();
	}

	@Override
	public PaymentMeanType meanType() {
		return this.origin.meanType();
	}

	@Override
	public String startNumber() {
		return this.origin.startNumber();
	}

	@Override
	public String endNumber() {
		return this.origin.endNumber();
	}

	@Override
	public String currentNumber() {
		return this.origin.currentNumber();
	}

	@Override
	public int totalNumberOfNotes() {
		return this.origin.totalNumberOfNotes();
	}

	@Override
	public int numberOfNotesUsed() {
		return this.origin.numberOfNotesUsed();
	}

	@Override
	public boolean has(String number) {
		return this.origin.has(number);
	}

	@Override
	public Iterable<BankNote> notesUsed() {
		return this.origin.notesUsed();
	}

	@Override
	public BankNoteBookStatus status() {
		return this.origin.status();
	}

	@Override
	public void activate() {
		this.origin.activate();
	}

	@Override
	public void block() {
		this.origin.block();
	}

	@Override
	public void terminate() {
		this.origin.terminate();
	}

	@Override
	public void update(final String startnumber, final String endnumber) {
		this.origin.update(startnumber, endnumber);
	}

	@Override
	public String name() {
		return this.origin.name();
	}

	@Override
	public String takeCurrentNumber() {
		return this.origin.takeCurrentNumber();
	}

	@Override
	public boolean hasNextNote() {
		return this.origin.hasNextNote();
	}

	@Override
	public String prefixNumber() {
		return this.origin.prefixNumber();
	}

	@Override
	public void prefixNumber(String prefix) {
		this.origin.prefixNumber(prefix);
	}

	@Override
	public String nextNoteAfter(String number) {
		return this.origin.nextNoteAfter(number);
	}

	@Override
	public boolean hasNextNoteAfter(String number) {
		return this.origin.hasNextNoteAfter(number);
	}
}
