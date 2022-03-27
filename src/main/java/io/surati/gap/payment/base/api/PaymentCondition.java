package io.surati.gap.payment.base.api;

public interface PaymentCondition {

	ThirdParty thirdParty();

	/**
	 * Deadline in days.
	 * @return days
	 */
	int deadline();
	
	void update(int deadline);
	
	boolean has(PaymentMeanType meantype);
	
	Iterable<PaymentMeanType> meanTypesAllowed();
	
	void add(PaymentMeanType meantype);
	
	void remove(PaymentMeanType meantype);
}
