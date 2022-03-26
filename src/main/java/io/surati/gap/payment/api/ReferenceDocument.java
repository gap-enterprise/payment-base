package io.surati.gap.payment.api;

import io.surati.gap.admin.api.ReferenceDocumentType;
import io.surati.gap.admin.api.User;
import io.surati.gap.commons.utils.convert.filter.Field;
import io.surati.gap.commons.utils.convert.filter.FieldDate;

import java.time.LocalDate;

public interface ReferenceDocument {
	
	Long id();
	
	ThirdParty issuer();

	ReferenceDocumentType type();
	
	LocalDate date();
	
	String reference();
	
	String otherReference();
	
	String object();
	
	String place();
	
	LocalDate depositDate();
	
	LocalDate entryDate();
	
	Double amount();
	
	Double advancedAmount();
	
	Double amountPaid();
	
	Double amountLeft();
	
	ReferenceDocumentStatus status();
	
	void type(ReferenceDocumentType type);

	void amount(Double amount, Double advamount);

	void update(LocalDate date, String reference, String object, String place);
	
	void update(String otherref);
	
	void update(LocalDate depositdate, LocalDate entrydate);
	
	Iterable<Payment> payments();
	
	ReferenceDocumentStep step();
	
	void sendToTreatment();
	
	void sendBackInPreparation();
	
	void sendInPayment();
	
	void archive();
	
	void updateState();
	
	User author();

	PaymentOrder preparePayment(User author);
	
	void beneficiary(ThirdParty beneficiary);
	
	static Field valueOf(final String value) {
		final Field field;
		switch (value) {
			case "DATE":
				field = ReferenceDocument.DATE;
				break;
			case "AMOUNT":
				field = ReferenceDocument.AMOUNT;
				break;
			case "BENEFICIARY":
				field = ReferenceDocument.BENEFICIARY;
				break;
			default:
				throw new IllegalArgumentException(String.format("Champ d'ordre (%s) non pris en charge", value));
		}
		return field;
	}
	FieldDate DATE = new FieldDate() {
		@Override
		public String name() {
			return "Date du document";
		}

		@Override
		public String id() {
			return "DATE";
		}
	};
	
	Field BENEFICIARY = new Field() {
		@Override
		public String name() {
			return "Bénéficiaire";
		}

		@Override
		public String id() {
			return "BENEFICIARY";
		}
	};
	
	Field AMOUNT = new Field() {	
		@Override
		public String name() {
			return "Montant";
		}

		@Override
		public String id() {
			return "AMOUNT";
		}
	};

	ReferenceDocument EMPTY = new ReferenceDocument() {
		
		@Override
		public void update(LocalDate depositdate, LocalDate entrydate) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void update(String otherref) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void update(LocalDate date, String reference, String object, String place) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public ReferenceDocumentType type() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ReferenceDocumentStatus status() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String reference() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public PaymentOrder preparePayment(User author) {
			return PaymentOrder.EMPTY;
		}
		
		@Override
		public String place() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Iterable<Payment> payments() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String object() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ThirdParty issuer() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public LocalDate entryDate() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public LocalDate depositDate() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public LocalDate date() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Double amountPaid() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Double amountLeft() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void amount(Double amount, Double advamount) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Double amount() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void type(ReferenceDocumentType type) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beneficiary(ThirdParty beneficiary) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ReferenceDocumentStep step() {
			return ReferenceDocumentStep.NONE;
		}

		@Override
		public void sendToTreatment() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendInPayment() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void archive() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendBackInPreparation() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateState() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String otherReference() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public User author() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double advancedAmount() {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
