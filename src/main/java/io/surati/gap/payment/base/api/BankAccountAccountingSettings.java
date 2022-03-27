package io.surati.gap.payment.base.api;

public interface BankAccountAccountingSettings {

	Iterable<BankAccountAccountingSetting> iterate();

	BankAccountAccountingSetting get(PaymentMeanType meantype);
}
