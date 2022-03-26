package io.surati.gap.payment.db;

import io.surati.gap.payment.api.ReferenceDocument;
import io.surati.gap.payment.api.ReferenceDocumentStep;
import io.surati.gap.payment.api.ReferenceDocumentsToTreat;
import io.surati.gap.payment.filter.ReferenceDocumentCriteria;

import javax.sql.DataSource;

public final class DbReferenceDocumentsToTreat implements ReferenceDocumentsToTreat {

	private final DataSource source;

	DbReferenceDocumentsToTreat(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public ReferenceDocument get(Long id) {
		final ReferenceDocumentCriteria criteria = new ReferenceDocumentCriteria();
		criteria.step(ReferenceDocumentStep.TO_TREAT);
		return new DbPaginedReferenceDocuments(this.source, criteria).get(id);
	}

	@Override
	public Iterable<ReferenceDocument> iterate(final ReferenceDocumentCriteria criteria) {
		criteria.step(ReferenceDocumentStep.TO_TREAT);
		return new DbPaginedReferenceDocuments(this.source, criteria).iterate();
	}

	@Override
	public Double amountToPay(final ReferenceDocumentCriteria criteria) {
		criteria.step(ReferenceDocumentStep.TO_TREAT);
		return new DbPaginedReferenceDocuments(this.source, criteria).amountLeft();
	}

	@Override
	public Long count(ReferenceDocumentCriteria criteria) {
		criteria.step(ReferenceDocumentStep.TO_TREAT);
		return new DbPaginedReferenceDocuments(this.source, criteria).count();
	}

}
