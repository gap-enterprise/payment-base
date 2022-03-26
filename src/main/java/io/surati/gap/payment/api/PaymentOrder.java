package io.surati.gap.payment.api;

import io.surati.gap.admin.api.User;

import java.time.LocalDate;

public interface PaymentOrder {

	Long id();

	LocalDate date();
	
	String reference();
	
	ThirdParty beneficiary();
	
	Double amountToPay();
	
	String amountToPayInHuman();

	ReferenceDocument document();
	
	void update(LocalDate date, ThirdParty beneficiary, double amounttopay, String reason, String description);
	
	void amount(double newamount);

	User author();
	
	User authorizingOfficer();
	
	PaymentOrderStatus status();
	
	void sendBackInPreparation();
	
	void validate(User author);
	
	void execute();
	
	void cancelExecution();
	
	void assign(PaymentMeanType meanType);
	
	void assign(BankAccount bank);
	
	void joinTo(ReferenceDocument document);
	
	PaymentOrder duplicate(User author);
	
	String reason();
	
	String description();
	
	PaymentOrder split(double firstamount, User author);
	
	PaymentOrder EMPTY = new PaymentOrder() {
		
		@Override
		public void validate(User author) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void update(LocalDate date, ThirdParty beneficiary, double amounttopay, String reason, String description) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public PaymentOrderStatus status() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String reference() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ReferenceDocument document() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public LocalDate date() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void cancelExecution() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public ThirdParty beneficiary() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public User authorizingOfficer() {
			return null;
		}
		
		@Override
		public void assign(BankAccount bank) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void assign(PaymentMeanType meanType) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String amountToPayInHuman() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Double amountToPay() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public User author() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void joinTo(ReferenceDocument document) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendBackInPreparation() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public PaymentOrder duplicate(User author) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String reason() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String description() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PaymentOrder split(double firstamount, User author) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void amount(double newamount) {
			// TODO Auto-generated method stub
			
		}
	};
}
