package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.User;

public interface ThirdPartyPaymentOrders extends PaymentOrders {
	
	PaymentOrder add(double amounttopay, String reason, String description, User author);
	
	PaymentOrder add(String reference, double amounttopay, String reason, String description, User author);
}
