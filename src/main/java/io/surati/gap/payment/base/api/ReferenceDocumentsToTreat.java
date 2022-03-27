package io.surati.gap.payment.base.api;

import io.surati.gap.payment.base.filter.ReferenceDocumentCriteria;

public interface ReferenceDocumentsToTreat {

	ReferenceDocument get(Long id);

	Iterable<ReferenceDocument> iterate(ReferenceDocumentCriteria criteria);
	
	Double amountToPay(ReferenceDocumentCriteria criteria);
	
	Long count(ReferenceDocumentCriteria criteria);
}
