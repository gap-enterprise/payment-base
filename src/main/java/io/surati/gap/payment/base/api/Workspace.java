package io.surati.gap.payment.base.api;

public interface Workspace {

	ReferenceDocumentsToTreat documentsToTreat();
	
	ReferenceDocumentsToPay documentsToPay();
	
	PaymentOrderGroupsToPrepare ordersToPrepare();
	
	PaymentOrderGroupsToExecute ordersToExecute();
	
    void sendToPreparation();
	
	void cancelSelection();
	
	void validate();
}
