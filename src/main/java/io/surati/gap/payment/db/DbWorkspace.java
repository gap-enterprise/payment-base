package io.surati.gap.payment.db;

import io.surati.gap.admin.api.User;
import io.surati.gap.payment.api.PaymentOrderGroupsToExecute;
import io.surati.gap.payment.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.api.ReferenceDocument;
import io.surati.gap.payment.api.ReferenceDocumentsToPay;
import io.surati.gap.payment.api.ReferenceDocumentsToTreat;
import io.surati.gap.payment.api.Workspace;

import javax.sql.DataSource;

public final class DbWorkspace implements Workspace {

	private final DataSource source;
	
	private final User user;
	
	private final ReferenceDocumentsToTreat documentstotreat;

	private final ReferenceDocumentsToPay documentstopay;
	
	private final PaymentOrderGroupsToPrepare orderstoprepare;
	
	private final PaymentOrderGroupsToExecute ordertoexecute;

	public DbWorkspace(final DataSource source, final User user) {
		this.source = source;
		this.user = user;
		this.documentstotreat = new DbReferenceDocumentsToTreat(this.source);
		this.documentstopay = new DbReferenceDocumentsToPay(this.source, this.user);
		this.orderstoprepare = new DbPaymentOrderGroupsToPrepare(this.source, this.user);
		this.ordertoexecute = new DbPaymentOrderGroupsToExecute(this.source, this.user);
	}

	@Override
	public ReferenceDocumentsToPay documentsToPay() {
		return this.documentstopay;
	}

	@Override
	public PaymentOrderGroupsToPrepare ordersToPrepare() {
		return this.orderstoprepare;
	}

	@Override
	public PaymentOrderGroupsToExecute ordersToExecute() {
		return this.ordertoexecute;
	}
	
	@Override
	public void sendToPreparation() {
		this.orderstoprepare.prepare(this.documentstopay.iterate());
	}

	@Override
	public void cancelSelection() {
		for (ReferenceDocument document : this.documentstopay.iterate()) {
			this.documentstopay.deselect(document);
		}
	}

	@Override
	public ReferenceDocumentsToTreat documentsToTreat() {
		return this.documentstotreat;
	}

	@Override
	public void validate() {
		this.orderstoprepare.validate();
	}
}
