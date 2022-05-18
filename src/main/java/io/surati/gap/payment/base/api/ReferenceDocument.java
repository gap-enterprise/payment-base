package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.convert.filter.Field;
import io.surati.gap.commons.utils.convert.filter.FieldDate;

import java.time.LocalDate;

public interface ReferenceDocument {
	
	Long id();
	
	ThirdParty beneficiary();

	ReferenceDocumentType type();
	
	LocalDate date();
	
	String reference();
	
	String otherReference();
	
	String object();
	
	String place();
	
	LocalDate depositDate();
	
	LocalDate entryDate();
	
	Double amount();
	
	Double amountPaid();
	
	Double amountLeft();

	Double priorAmountPaid();
	
	ReferenceDocumentStatus status();
	
	void type(ReferenceDocumentType type);

	void amount(Double amount, Double advamount);

	void update(LocalDate date, String reference, String object, String place);
	
	void update(String internalref);
	
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
			
			
		}
		
		@Override
		public void update(String otherref) {
			
			
		}
		
		@Override
		public void update(LocalDate date, String reference, String object, String place) {
			
			
		}
		
		@Override
		public ReferenceDocumentType type() {
			
			return null;
		}
		
		@Override
		public ReferenceDocumentStatus status() {
			
			return null;
		}
		
		@Override
		public String reference() {
			
			return null;
		}
		
		@Override
		public PaymentOrder preparePayment(User author) {
			return PaymentOrder.EMPTY;
		}
		
		@Override
		public String place() {
			
			return null;
		}
		
		@Override
		public Iterable<Payment> payments() {
			
			return null;
		}
		
		@Override
		public String object() {
			
			return null;
		}
		
		@Override
		public ThirdParty beneficiary() {
			
			return null;
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public LocalDate entryDate() {
			
			return null;
		}
		
		@Override
		public LocalDate depositDate() {
			
			return null;
		}
		
		@Override
		public LocalDate date() {
			
			return null;
		}
		
		@Override
		public Double amountPaid() {
			
			return null;
		}
		
		@Override
		public Double amountLeft() {
			
			return null;
		}

		@Override
		public Double priorAmountPaid() {
			return null;
		}

		@Override
		public void amount(Double amount, Double advamount) {
			
			
		}
		
		@Override
		public Double amount() {
			
			return null;
		}

		@Override
		public void type(ReferenceDocumentType type) {
			
			
		}

		@Override
		public void beneficiary(ThirdParty beneficiary) {
			
			
		}

		@Override
		public ReferenceDocumentStep step() {
			return ReferenceDocumentStep.NONE;
		}

		@Override
		public void sendToTreatment() {
			
			
		}

		@Override
		public void sendInPayment() {
			
			
		}

		@Override
		public void archive() {
			
			
		}

		@Override
		public void sendBackInPreparation() {
			
			
		}

		@Override
		public void updateState() {
			
			
		}

		@Override
		public String otherReference() {
			
			return null;
		}

		@Override
		public User author() {
			
			return null;
		}
	};
}
