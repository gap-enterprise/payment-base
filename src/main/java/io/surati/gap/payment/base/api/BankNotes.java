package io.surati.gap.payment.base.api;

public interface BankNotes {

	Iterable<BankNote> iterate();
	
	Iterable<BankNote> iterate(PaymentStatus status);
	
	BankNote get(Long id);

	Long count();
}
