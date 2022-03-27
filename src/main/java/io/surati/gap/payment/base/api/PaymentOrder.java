package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.User;

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
			
			
		}
		
		@Override
		public void update(LocalDate date, ThirdParty beneficiary, double amounttopay, String reason, String description) {
			
			
		}
		
		@Override
		public PaymentOrderStatus status() {
			
			return null;
		}
		
		@Override
		public String reference() {
			
			return null;
		}
		
		@Override
		public ReferenceDocument document() {
			
			return null;
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public LocalDate date() {
			
			return null;
		}
		
		@Override
		public void cancelExecution() {
			
			
		}
		
		@Override
		public ThirdParty beneficiary() {
			
			return null;
		}
		
		@Override
		public User authorizingOfficer() {
			return null;
		}
		
		@Override
		public void assign(BankAccount bank) {
			
			
		}
		
		@Override
		public void assign(PaymentMeanType meanType) {
			
			
		}
		
		@Override
		public String amountToPayInHuman() {
			
			return null;
		}
		
		@Override
		public Double amountToPay() {
			
			return null;
		}

		@Override
		public User author() {
			
			return null;
		}

		@Override
		public void joinTo(ReferenceDocument document) {
			
			
		}

		@Override
		public void sendBackInPreparation() {
			
			
		}

		@Override
		public PaymentOrder duplicate(User author) {
			
			return null;
		}

		@Override
		public void execute() {
			
			
		}

		@Override
		public String reason() {
			
			return null;
		}

		@Override
		public String description() {
			
			return null;
		}

		@Override
		public PaymentOrder split(double firstamount, User author) {
			
			return null;
		}

		@Override
		public void amount(double newamount) {
			
			
		}
	};
}
