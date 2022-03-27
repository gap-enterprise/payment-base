package io.surati.gap.payment.base.api;

public interface PaymentOrderGroupsToExecute {

	PaymentOrderGroup get(Long id);

	Iterable<PaymentOrderGroup> iterate();
	
	void sendBackInPreparation();
	
	Double totalAmount();
}
