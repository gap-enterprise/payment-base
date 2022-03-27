package io.surati.gap.payment.base.api;

import java.time.LocalDate;

public interface PaymentBatches {

	PaymentBatch get(Long id);

	PaymentBatch get(BankAccount account, PaymentMeanType meantype);

	Iterable<PaymentBatch> iterate();
	
	Double totalAmount();
	
	PaymentBatch add(LocalDate date, BankAccount account, PaymentMeanType meantype);
}
