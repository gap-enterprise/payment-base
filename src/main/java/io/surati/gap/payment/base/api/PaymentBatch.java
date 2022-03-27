package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.User;

import java.time.LocalDate;

public interface PaymentBatch {
	
	Long id();
	
	LocalDate date();

	BankAccount account();
	
	PaymentMeanType meanType();
	
	PaymentStatus status();
	
	Iterable<PaymentOrderGroup> groups();
	
	Iterable<BankNote> notes();

	void add(PaymentOrderGroup group);
	
	BankNote pay(PaymentOrderGroup group, Iterable<BankNoteBook> books, User author);
	
	void terminate();
}
