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
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void useAccount(BankAccount account) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Double totalAmount() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public PaymentOrderStatus status() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void sendBackInPreparation() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Iterable<PaymentOrder> iterate() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long id() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public boolean has(PaymentOrder item) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public PaymentOrder get(Long id) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long count() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ThirdParty beneficiary() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public BankAccount accountToUse() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void cancelExecution(final User author, final boolean sentbackinpayment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public PaymentMeanType meanType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public LocalDate dueDate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void update(PaymentMeanType meantype, LocalDate duedate) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isHetero() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void changeBeneficiary(ThirdParty tp) {
			// TODO Auto-generated method stub
			
		}
	};
}
