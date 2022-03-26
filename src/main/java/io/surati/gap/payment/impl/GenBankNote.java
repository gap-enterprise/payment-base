package io.surati.gap.payment.impl;

import com.baudoliver7.fr.FrIntegerInLetters;
import io.surati.gap.admin.api.User;
import io.surati.gap.admin.prop.PropCompany;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.PaymentCancelReason;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentStatus;
import io.surati.gap.payment.api.ThirdParty;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class GenBankNote implements BankNote {

	private final PaymentOrderGroup orders;

	private final BankNoteBook book;
	
	private final String currentnumber;
	
	public GenBankNote(final PaymentOrderGroup orders, final BankNoteBook book, final String currentnumber) {
		this.orders = orders;
		this.book = book;
		this.currentnumber = currentnumber;
	}

	@Override
	public Long id() {
		return this.orders.id();
	}

	@Override
	public PaymentMeanType meanType() {
		return PaymentMeanType.NONE;
	}

	@Override
	public String internalReference() {
		return StringUtils.EMPTY;
	}

	@Override
	public String issuerReference() {
		return this.currentnumber;
	}

	@Override
	public String name() {
		return StringUtils.EMPTY;
	}

	@Override
	public BankNoteBook book() {
		return this.book;
	}

	@Override
	public LocalDate date() {
		return LocalDate.now();
	}

	@Override
	public ThirdParty beneficiary() {
		return this.orders.beneficiary();
	}

	@Override
	public Double amount() {
		return this.orders.totalAmount();
	}

	@Override
	public String amountInHuman() {
		return new FrAmountInXof(this.amount()).toString();
	}

	@Override
	public String amountInLetters() {
		return new FrIntegerInLetters(this.amount().longValue()).toString();
	}

	@Override
	public String place() {
		return new PropCompany().city();
	}

	@Override
	public PaymentStatus status() {
		return PaymentStatus.NONE;
	}

	@Override
	public LocalDate dueDate() {
		return this.orders.dueDate();
	}

	@Override
	public String mention1() {
		return StringUtils.EMPTY;
	}

	@Override
	public String mention2() {
		return StringUtils.EMPTY;
	}

	@Override
	public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, boolean sendbackinpayment, User author) {
		throw new UnsupportedOperationException("GenBankNote#cancel");
	}

	@Override
	public PaymentCancelReason reasonOfCancel() {
		return PaymentCancelReason.NONE;
	}

	@Override
	public String descriptionOfCancel() {
		return StringUtils.EMPTY;
	}

	@Override
	public User authorOfCancel() {
		return User.EMPTY;
	}

	@Override
	public User author() {
		return User.EMPTY;
	}

	@Override
	public ThirdParty issuer() {
		return ThirdParty.EMPTY;
	}

	@Override
	public void complete() {
		throw new UnsupportedOperationException("GenBankNote#complete");
	}

	@Override
	public PaymentOrderGroup orders() {
		return this.orders;
	}

	@Override
	public LocalDateTime cancelDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
