package io.surati.gap.payment.wp;

import io.surati.gap.admin.api.User;
import io.surati.gap.payment.api.Payment;
import io.surati.gap.payment.api.PaymentCancelReason;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentStatus;
import io.surati.gap.payment.api.ThirdParty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class PaymentWrapper implements Payment {

	private final Payment origin;

	public PaymentWrapper(final Payment origin) {
		this.origin = origin;
	}
	
	@Override
	public Long id() {
		return this.origin.id();
	}

	@Override
	public String issuerReference() {
		return this.origin.issuerReference();
	}

	@Override
	public LocalDate date() {
		return this.origin.date();
	}

	@Override
	public ThirdParty beneficiary() {
		return this.origin.beneficiary();
	}

	@Override
	public Double amount() {
		return this.origin.amount();
	}

	@Override
	public PaymentStatus status() {
		return this.origin.status();
	}

	@Override
	public String mention1() {
		return this.origin.mention1();
	}

	@Override
	public String mention2() {
		return this.origin.mention2();
	}

	@Override
	public void cancel(final LocalDateTime canceldate, final PaymentCancelReason reason, final String description, final boolean sendbackinpayment, final User author) {
		this.origin.cancel(canceldate, reason, description, sendbackinpayment, author);
	}

	@Override
	public LocalDateTime cancelDate() {
		return this.origin.cancelDate();
	}

	@Override
	public PaymentCancelReason reasonOfCancel() {
		return this.origin.reasonOfCancel();
	}

	@Override
	public String name() {
		return this.origin.name();
	}

	@Override
	public String place() {
		return this.origin.place();
	}

	@Override
	public User author() {
		return this.origin.author();
	}

	@Override
	public ThirdParty issuer() {
		return this.origin.issuer();
	}

	@Override
	public String amountInLetters() {
		return this.origin.amountInLetters();
	}

	@Override
	public String amountInHuman() {
		return this.origin.amountInHuman();
	}

	@Override
	public void complete() {
		this.origin.complete();
	}

	@Override
	public String internalReference() {
		return this.origin.internalReference();
	}

	@Override
	public PaymentOrderGroup orders() {
		return this.origin.orders();
	}

	@Override
	public String descriptionOfCancel() {
		return this.origin.descriptionOfCancel();
	}

	@Override
	public User authorOfCancel() {
		return this.origin.authorOfCancel();
	}

	@Override
	public PaymentMeanType meanType() {
		return this.origin.meanType();
	}
}
