package io.surati.gap.payment.base.api;

import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.admin.base.api.User;

import java.time.LocalDate;

public interface ThirdPartyReferenceDocuments extends ReferenceDocuments {

	boolean has(String reference, ReferenceDocumentType type);
	
	boolean has(String otherreference);
	
	ReferenceDocument add(LocalDate date, ReferenceDocumentType type, String reference, double amount, String object, String place, User author);
	
	ReferenceDocument add(ReferenceDocumentType type, String reference, double amount, String object, String place, User author);
}
