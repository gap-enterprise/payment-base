package io.surati.gap.payment.api;

public interface PaymentExport {
	
	Iterable<Payment> iterate();
	
	void add(Payment payment);
	
	void finish();
}
