package io.surati.gap.payment.base.filter;

import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.commons.utils.convert.filter.SearchCriteria;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ThirdParty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedList;

public final class ReferenceDocumentCriteria extends SearchCriteria {
	
	private ReferenceDocumentStep step;
	
	private final ReferenceDocumentType type;
	
	private final ThirdParty issuer;
	
	private String reference;
	
	private String otherreference;
	
	private Collection<ReferenceDocumentStatus> statusavailables;
	
	public ReferenceDocumentCriteria() {
		this(ThirdParty.EMPTY, ReferenceDocumentType.NONE);
	}
	public ReferenceDocumentCriteria(
		final ThirdParty issuer,
		final ReferenceDocumentType type
	) {
		super();
		this.issuer = issuer;
		this.type = type;
		this.reference = StringUtils.EMPTY;
		this.otherreference = StringUtils.EMPTY;
		this.statusavailables = new LinkedList<>();
		this.step = ReferenceDocumentStep.NONE;
	}
	
	public ThirdParty issuer() {
		return this.issuer;
	}
	
	public ReferenceDocumentType type() {
		return this.type;
	}
	
	public ReferenceDocumentStep step() {
		return this.step;
	}
	
	public void step(final ReferenceDocumentStep step) {
		this.step = step;
	}
	public void reference(String reference) {
		this.reference = reference;
	}
	
	public void otherReference(String reference) {
		this.otherreference = reference;
	}
	
	public String reference() {
		return this.reference;
	}
	
	public String otherReference() {
		return this.otherreference;
	}
	
	public void add(ReferenceDocumentStatus status) {
		this.statusavailables.add(status);
	}
	
	public void add(Collection<ReferenceDocumentStatus> status) {
		this.statusavailables.addAll(status);
	}

	public Iterable<ReferenceDocumentStatus> statusAvailables() {
		return this.statusavailables;
	}
}
