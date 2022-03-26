package io.surati.gap.payment.api;

import io.surati.gap.admin.api.User;

public interface ThirdPartyPaymentOrders extends PaymentOrders {
	
	PaymentOrder add(double amounttopay, String reason, String description, User author);
	
	PaymentOrder add(String reference, double amounttopay, String reason, String description, User author);
}
