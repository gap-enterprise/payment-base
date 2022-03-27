package io.surati.gap.payment.base.impl;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class PaymentBatchImpl implements PaymentBatch {

	private final Long order;
	
	private final BankAccount account;
	
	private final PaymentMeanType meantype;

	private final Collection<PaymentOrderGroup> groups;

	private final List<BankNote> notes;

	public PaymentBatchImpl(final Long order, final BankAccount account, final PaymentMeanType meantype) {
		this.order = order;
		this.account = account;
		this.meantype = meantype;
		this.groups = new LinkedList<>();
		this.notes = new LinkedList<>();
	}

	@Override
	public Long id() {
		return this.order;
	}

	@Override
	public BankAccount account() {
		return this.account;
	}

	@Override
	public PaymentMeanType meanType() {
		return this.meantype;
	}

	@Override
	public Iterable<PaymentOrderGroup> groups() {
		return this.groups
			.stream()
			.sorted(
				(g1, g2) -> g1.beneficiary().name()
					.compareToIgnoreCase(
						g2.beneficiary().name()
					)
			).collect(
				Collectors.toList()
			);
	}

	@Override
	public Iterable<BankNote> notes() {
		return this.notes;
	}
	
	@Override
	public void add(PaymentOrderGroup group) {
		this.groups.add(group);
	}

	@Override
	public BankNote pay(final PaymentOrderGroup group, final Iterable<BankNoteBook> books, final User author) {
		final Iterator<BankNoteBook> it = books.iterator();
		if(!it.hasNext()) {
			throw new IllegalArgumentException(
				"Vous devez choisir au moins un carnet avant de continuer !"
			);
		}
		BankNoteBook book = it.next();
		final String currentnumber;
		if(notes.isEmpty()) {
			currentnumber = book.currentNumber();
		} else {
			final String precnumber = notes.get(notes.size() - 1).issuerReference();
			if(book.hasNextNoteAfter(precnumber)) {
				currentnumber = book.nextNoteAfter(precnumber);
			} else {
				if(!it.hasNext()) {
					final int nbnotesleft = this.groups.size() - this.notes.size();
					throw new IllegalArgumentException(
						String.format(
							"Il vous manque %s formules à imprimer ! Vous devez mettre en utilisation un autre carnet avant de continuer.",
							nbnotesleft
						)
					);
				}
				book = it.next();
				if(book.has(precnumber)) {
					currentnumber = book.nextNoteAfter(precnumber);
				} else {
					currentnumber = book.currentNumber();
				}
			}
		}
		final BankNote note = new GenBankNote(group, book, currentnumber);
		notes.add(note);
		return note;
	}

	@Override
	public LocalDate date() {
		return LocalDate.now();
	}

	@Override
	public PaymentStatus status() {
		return PaymentStatus.NONE;
	}

	@Override
	public void terminate() {
		throw new UnsupportedOperationException("PaymentBatchImpl#terminate");
	}

}
