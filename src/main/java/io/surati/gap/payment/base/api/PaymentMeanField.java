package io.surati.gap.payment.base.api;

public interface PaymentMeanField {
	String name();
	PaymentMeanFieldType type();
	PaymentMean mean();
	Point location();
	double width();
	boolean visible();
	void update(Point location, double width);
	void show();
	void disappear();
}
