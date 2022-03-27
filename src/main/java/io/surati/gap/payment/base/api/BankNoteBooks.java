package io.surati.gap.payment.base.api;

/**
 * List of bank note books.
 * @since 3.0
 */
public interface BankNoteBooks {
	
	Iterable<BankNoteBook> iterate();
	
	Iterable<BankNoteBook> iterate(BankNoteBookStatus status);
	
	BankNoteBook get(Long id);
	
	void remove(Long id);

	Long count();
}
