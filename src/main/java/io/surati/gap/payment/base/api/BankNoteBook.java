package io.surati.gap.payment.base.api;

public interface BankNoteBook {

	Long id();

	String name();
	
	BankAccount account();
	
	PaymentMeanType meanType();

	String prefixNumber();

	String startNumber();
	
	String endNumber();
	
	String currentNumber();
	
	String takeCurrentNumber();
	
	int totalNumberOfNotes();
	
	int numberOfNotesUsed();
	
	boolean has(String number);
	
	boolean hasNextNote();

	String nextNoteAfter(String number);
	
	boolean hasNextNoteAfter(String number);
	
	Iterable<BankNote> notesUsed();
	
	BankNoteBookStatus status();
	
	void activate();

	void block();
	
	void terminate();
	
	void update(String startnumber, String endnumber);
	
	void prefixNumber(String prefix);
	
	BankNoteBook EMPTY = new BankNoteBook() {
		
		@Override
		public void update(String startnumber, String endnumber) {
			
			
		}
		
		@Override
		public int totalNumberOfNotes() {
			
			return 0;
		}
		
		@Override
		public void terminate() {
			
			
		}
		
		@Override
		public String takeCurrentNumber() {
			
			return null;
		}
		
		@Override
		public BankNoteBookStatus status() {
			
			return null;
		}
		
		@Override
		public String startNumber() {
			
			return null;
		}
		
		@Override
		public int numberOfNotesUsed() {
			
			return 0;
		}
		
		@Override
		public Iterable<BankNote> notesUsed() {
			
			return null;
		}
		
		@Override
		public String name() {
			
			return null;
		}
		
		@Override
		public PaymentMeanType meanType() {
			
			return null;
		}
		
		@Override
		public Long id() {
			
			return null;
		}
		
		@Override
		public boolean hasNextNote() {
			
			return false;
		}
		
		@Override
		public boolean has(String number) {
			
			return false;
		}
		
		@Override
		public String endNumber() {
			
			return null;
		}
		
		@Override
		public String currentNumber() {
			
			return null;
		}
		
		@Override
		public void block() {
			
			
		}
		
		@Override
		public void activate() {
			
			
		}
		
		@Override
		public BankAccount account() {
			
			return null;
		}

		@Override
		public String prefixNumber() {
			
			return null;
		}

		@Override
		public void prefixNumber(String prefix) {
			
			
		}

		@Override
		public String nextNoteAfter(String number) {
			
			return null;
		}

		@Override
		public boolean hasNextNoteAfter(String number) {
			
			return false;
		}
	};
}
