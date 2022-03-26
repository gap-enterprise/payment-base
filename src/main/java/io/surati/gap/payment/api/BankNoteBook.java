package io.surati.gap.payment.api;

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
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int totalNumberOfNotes() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void terminate() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String takeCurrentNumber() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public BankNoteBookStatus status() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String startNumber() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int numberOfNotesUsed() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Iterable<BankNote> notesUsed() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public PaymentMeanType meanType() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long id() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public boolean hasNextNote() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean has(String number) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public String endNumber() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String currentNumber() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void block() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void activate() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public BankAccount account() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String prefixNumber() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void prefixNumber(String prefix) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String nextNoteAfter(String number) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasNextNoteAfter(String number) {
			// TODO Auto-generated method stub
			return false;
		}
	};
}
