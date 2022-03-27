package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Payment {
	
	Long id();

	PaymentMeanType meanType();
	
	String internalReference();
	
	String issuerReference();
	
	String name();

	LocalDate date();
	
	ThirdParty beneficiary();
	
	Double amount();
	
	String amountInHuman();

	String amountInLetters();
	
	String place();
	
	PaymentStatus status();
	
	String mention1();
	
	String mention2();
	
	LocalDateTime cancelDate();
	
	void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, boolean sendbackinpayment, User author);
	
	PaymentCancelReason reasonOfCancel();
	
	String descriptionOfCancel();
	
	User authorOfCancel();
	
	User author();

	ThirdParty issuer();
	
	void complete();
	
	PaymentOrderGroup orders();
	
	Payment EMPTY = new Payment() {
		
		@Override
		public PaymentStatus status() {
			
			return null;
		}
		
		@Override
		public PaymentCancelReason reasonOfCancel() {
			
			return null;
		}
		
		@Override
		public String place() {
			
			return null;
		}
		
		@Override
		public String issuerReference() {
			
			return null;
		}
		
		@Override
		public String name() {
			
			return null;
		}
		
		@Override
		public String mention2() {
			
			return null;
		}
		
		@Override
		public String mention1() {
			
			return null;
		}
		
		@Override
		public User author() {
			
			return null;
		}

		@Override
		public ThirdParty issuer() {
			return null;
		}

		@Override
		public Long id() {
			
			return null;
		}

		@Override
		public PaymentMeanType meanType() {
			return null;
		}

		@Override
		public LocalDate date() {
			
			return null;
		}
		
		@Override
		public void complete() {
			
			
		}
		
		@Override
		public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, boolean sendbackinpayment, User author) {
			
			
		}
		
		@Override
		public ThirdParty beneficiary() {
			
			return null;
		}
		
		@Override
		public String amountInLetters() {
			
			return null;
		}
		
		@Override
		public String amountInHuman() {
			
			return null;
		}
		
		@Override
		public Double amount() {
			
			return null;
		}

		@Override
		public String internalReference() {
			
			return null;
		}

		@Override
		public PaymentOrderGroup orders() {
			return PaymentOrderGroup.EMPTY;
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
	};
}
