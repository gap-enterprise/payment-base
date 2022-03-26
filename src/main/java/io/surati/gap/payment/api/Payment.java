package io.surati.gap.payment.api;

import io.surati.gap.admin.api.User;

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
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public PaymentCancelReason reasonOfCancel() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String place() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String issuerReference() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String mention2() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String mention1() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public User author() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ThirdParty issuer() {
			return null;
		}

		@Override
		public Long id() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PaymentMeanType meanType() {
			return null;
		}

		@Override
		public LocalDate date() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void complete() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void cancel(LocalDateTime canceldate, PaymentCancelReason reason, String description, boolean sendbackinpayment, User author) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public ThirdParty beneficiary() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String amountInLetters() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String amountInHuman() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Double amount() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String internalReference() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PaymentOrderGroup orders() {
			return PaymentOrderGroup.EMPTY;
		}

		@Override
		public String descriptionOfCancel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public User authorOfCancel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public LocalDateTime cancelDate() {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
