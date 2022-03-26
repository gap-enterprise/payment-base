package io.surati.gap.payment.api;

public interface BankAccountAccountingSetting {

	BankAccount account();

	PaymentMeanType meanType();
	
	String journalCode();
	
	void update(String journalcode);
}
