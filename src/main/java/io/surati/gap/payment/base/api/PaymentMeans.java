package io.surati.gap.payment.base.api;

public interface PaymentMeans {
	Iterable<PaymentMean> iterate();
	PaymentMean get(Long id);
	PaymentMean get(PaymentMeanType type);
}
