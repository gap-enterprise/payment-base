package io.surati.gap.payment.base.impl;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.PaymentCancelReason;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentStatus;
import io.surati.gap.payment.base.api.ThirdParty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

public final class LettreChangeSample implements BankNote {

	@Override
	public Long id() {
		return 0L;
	}

	@Override
	public PaymentMeanType meanType() {
		return PaymentMeanType.CHQ;
	}

	@Override
	public String internalReference() {
		return "PAY/2021/0001";
	}

	@Override
	public String issuerReference() {
		return "0000007";
	}

	@Override
	public String name() {
		return "Chèque N°0000007";
	}

	@Override
	public BankNoteBook book() {
		return BankNoteBook.EMPTY;
	}

	@Override
	public LocalDate date() {
		return LocalDate.of(2021, 05, 20);
	}

	@Override
	public ThirdParty beneficiary() {
		return new ThirdPartySample();
	}

	@Override
	public Double amount() {
		return 18574957.0;
	}

	@Override
	public String amountInHuman() {
		return "18 574 957 FCFA";
	}

	@Override
	public String amountInLetters() {
		return "Dix-huit Million Cinq Cent Soixante-Quatorze Mille Neuf Cent Cinquante Sept Francs CFA";
	}

	@Override
	public String place() {
		return "Abidjan";
	}

	@Override
	public PaymentStatus status() {
		return PaymentStatus.TO_PRINT;
	}

	@Override
	public LocalDate dueDate() {
		return LocalDate.of(2021, 06, 01);
	}

	@Override
	public String mention1() {
		return "Mention supplémentaire 1";
	}

	@Override
	public String mention2() {
		return "Mention supplémentaire 2";
	}

	@Override
	public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, final boolean sendbackinpayment, final User author) {
		throw new UnsupportedOperationException("LettreChangeSample#cancel");
	}

	@Override
	public PaymentCancelReason reasonOfCancel() {
		return null;
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
		throw new UnsupportedOperationException("LettreChangeSample#complete");
	}
	
	@Override
	public Iterable<PaymentOrder> orders() {
		return Collections.emptyList();
	}

	@Override
	public String descriptionOfCancel() {
		
		return null;
	}

	@Override
	public User authorOfCancel() {
		
		return null;
	}

	@Override
	public LocalDateTime cancelDate() {
		
		return null;
	}
}
