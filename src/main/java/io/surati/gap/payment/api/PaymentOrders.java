package io.surati.gap.payment.api;

public interface PaymentOrders {
	
	Iterable<PaymentOrder> iterate();

	PaymentOrder get(Long id);
	
	PaymentOrder get(String reference);
	
	void remove(PaymentOrder item);

	Long count();
	
	Double totalAmount();
	
	boolean has(String reference);
}
