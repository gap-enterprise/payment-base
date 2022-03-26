package io.surati.gap.payment.api;

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
