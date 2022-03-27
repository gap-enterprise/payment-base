package io.surati.gap.payment.base.api;

public interface PaymentOrderGroupsToPrepare {

	PaymentOrderGroup get(Long id);

	Iterable<PaymentOrderGroup> iterate();
	
	Iterable<PaymentOrderGroup> prepare(Iterable<ReferenceDocument> documents);

	PaymentOrderGroup merge(Iterable<PaymentOrder> orders);
	
	PaymentOrderGroup mergeAcross(ThirdParty beneficiary, Iterable<PaymentOrder> orders);
	
	void merge(PaymentOrderGroup group, Iterable<PaymentOrder> orders);
	
	void merge(PaymentOrderGroup group, PaymentOrder order);
	
	void validate();
	
	Double totalAmount();
}
