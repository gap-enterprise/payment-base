package io.surati.gap.payment.api;

import io.surati.gap.admin.api.User;

import java.time.LocalDate;

public interface PaymentOrderGroup {
	
	Long id();
	
	ThirdParty beneficiary();
	
	Iterable<PaymentOrder> iterate();

	PaymentOrder get(Long id);
	
	boolean has(PaymentOrder item);

	Long count();
	
	Double totalAmount();
	
	BankAccount accountToUse();
	
	void useAccount(BankAccount account);
	
	void validate(User author);
	
	void sendBackInPreparation();
	
	void execute();
	
	void cancelExecution(User author, final boolean sentbackinpayment);
	
    void changeBeneficiary(ThirdParty tp);

	PaymentOrderStatus status();
    
    PaymentMeanType meanType();

	LocalDate dueDate();
	
	/**
	 * Can accept orders of different beneficiaries.
	 * @return
	 */
	boolean isHetero();
	
	void update(PaymentMeanType meantype, LocalDate duedate);
	
    PaymentOrderGroup EMPTY = new PaymentOrderGroup() {
		
		@Override
		public void validate(User author) {
			
			
		}
		
		@Override
		public void useAccount(BankAccount account) {
			
			
		}
		
		@Override
		public Double totalAmount() {
			
			return null;
		}
		
		@Override
		public PaymentOrderStatus status() {
			
			return null;
		}
		
		@Override
		public void sendBackInPreparation() {
			
			
		}
		
		@Override
		public Iterable<PaymentOrder> iterate() {
			
			return null;
		}
		
		@Override
		public Long id() {
			
			return null;
		}
		
		@Override
		public boolean has(PaymentOrder item) {
			
			return false;
		}
		
		@Override
		public PaymentOrder get(Long id) {
			
			return null;
		}
		
		@Override
		public Long count() {
			
			return null;
		}
		
		@Override
		public ThirdParty beneficiary() {
			
			return null;
		}
		
		@Override
		public BankAccount accountToUse() {
			
			return null;
		}

		@Override
		public void execute() {
			
			
		}

		@Override
		public void cancelExecution(final User author, final boolean sentbackinpayment) {
			
			
		}

		@Override
		public PaymentMeanType meanType() {
			
			return null;
		}

		@Override
		public LocalDate dueDate() {
			
			return null;
		}

		@Override
		public void update(PaymentMeanType meantype, LocalDate duedate) {
			
			
		}

		@Override
		public boolean isHetero() {
			
			return false;
		}

		@Override
		public void changeBeneficiary(ThirdParty tp) {
			
			
		}
	};
}
