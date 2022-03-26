package io.surati.gap.payment.api;

public interface BankAccountAccountingSettings {

	Iterable<BankAccountAccountingSetting> iterate();

	BankAccountAccountingSetting get(PaymentMeanType meantype);
}
