package io.surati.gap.payment.base.api;

public interface PaymentMean {

	Long id();
	
	String name();
	
	PaymentMeanType type();
	
	Bank bank();
	
	Image image();
	
	Iterable<PaymentMeanField> fields();
	
	PaymentMeanField field(PaymentMeanFieldType type);
}
