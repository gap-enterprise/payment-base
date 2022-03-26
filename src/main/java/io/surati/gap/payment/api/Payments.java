package io.surati.gap.payment.api;

public interface Payments {

	Iterable<Payment> iterate();
	
	Iterable<Payment> iterate(PaymentStatus status);

	Payment get(Long id);

	Long count();
}
