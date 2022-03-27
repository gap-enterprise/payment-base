package io.surati.gap.payment.base.api;

public interface ReferenceDocumentsToPay {

	ReferenceDocument get(Long id);

	Iterable<ReferenceDocument> iterate();
	
	void select(ReferenceDocument document);
	
	void select(Iterable<ReferenceDocument> documents);
	
	void deselect(ReferenceDocument document);
	
	Double amountToPay();
}
