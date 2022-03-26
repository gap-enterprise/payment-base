package io.surati.gap.payment.api;

public interface ReferenceDocuments {

	ReferenceDocument get(Long id);

	Iterable<ReferenceDocument> iterate();

	void remove(ReferenceDocument item);
	
	Long count();
	
	Double totalAmount();
	
	Double amountLeft();
	
	boolean hasAny();
	
	ReferenceDocument first();
}
