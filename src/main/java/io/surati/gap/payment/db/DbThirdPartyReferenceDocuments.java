package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.api.ReferenceDocumentType;
import io.surati.gap.admin.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.ReferenceDocument;
import io.surati.gap.payment.api.ReferenceDocumentStatus;
import io.surati.gap.payment.api.ReferenceDocumentStep;
import io.surati.gap.payment.api.ReferenceDocuments;
import io.surati.gap.payment.api.ThirdParty;
import io.surati.gap.payment.api.ThirdPartyReferenceDocuments;
import io.surati.gap.payment.filter.ReferenceDocumentCriteria;
import org.apache.commons.lang.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;

public final class DbThirdPartyReferenceDocuments implements ThirdPartyReferenceDocuments {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	private final ThirdParty issuer;
	
	private final ReferenceDocuments origin;
	
	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbThirdPartyReferenceDocuments(final DataSource source, final ThirdParty issuer) {
		this(source, issuer, ReferenceDocumentType.NONE, ReferenceDocumentStatus.NONE);
	}
	
	/**
	 * Ctor.
	 * @param source Data source
	 */
	public DbThirdPartyReferenceDocuments(final DataSource source, final ThirdParty issuer, final ReferenceDocumentType type, final ReferenceDocumentStatus status) {
		this.source = source;
		this.issuer = issuer;
		final ReferenceDocumentCriteria criteria =
			new ReferenceDocumentCriteria(
				this.issuer,
				type
			);
		criteria.add(status);
		this.origin = new DbPaginedReferenceDocuments(this.source, criteria);
	}

	@Override
	public Iterable<ReferenceDocument> iterate() {
		return this.origin.iterate();
	}

	@Override
	public ReferenceDocument get(final Long id) {
		return this.origin.get(id);
	}

	@Override
	public void remove(final ReferenceDocument item) {
		this.origin.remove(item);
	}

	@Override
	synchronized public ReferenceDocument add(
		final ReferenceDocumentType type,
		final String reference,
		final double amount,
		final String object,
		final String place,
		final User author
	) {
		return this.add(LocalDate.now(), type, reference, amount, object, place, author);
	}

	@Override
	synchronized public ReferenceDocument add(
		final LocalDate date,
		final ReferenceDocumentType type,
		final String reference,
		final double amount,
		final String object,
		final String place,
		final User author
	) {
		if(author == User.EMPTY) {
			throw new IllegalArgumentException("Un autheur pour cette action est exigée !");
		}
		if(StringUtils.isBlank(reference)) {
			throw new IllegalArgumentException("Vous devez renseigner la référence du document !");
		}
		if(StringUtils.isBlank(place)) {
			throw new IllegalArgumentException("Vous devez renseigner le lieu d'édition du document !");
		}
		if(StringUtils.isBlank(object)) {
			throw new IllegalArgumentException("Vous devez renseigner l'objet du document !");
		}
		if(type == ReferenceDocumentType.NONE) {
			throw new IllegalArgumentException("Vous devez spécifier le type du document !");
		}
		if(this.has(reference, type)) {
			throw new IllegalArgumentException(String.format("La référence du document (%s N°%s) est déjà utilisée par le tiers (%s)!", type, reference, this.issuer.abbreviated()));
		}
		try {
			final ReferenceDocument document = new DbReferenceDocument(
	            this.source,
	    		new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "INSERT INTO pay_reference_document",
		                    "(date, type_id, reference, object, place, issuer_id, amount, status_id, step_id, amount_paid, amount_left, author_id, entry_date, worker_id)",
		                    "VALUES",
		                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, ?)"
		                ).toString()
		            )
		            .set(java.sql.Date.valueOf(date))
		            .set(type.name())
		            .set(reference)
		            .set(object)
		            .set(place)
		            .set(this.issuer.id())
		            .set(amount)
		            .set(ReferenceDocumentStatus.WAITING_FOR_PAYMENT.name())
		            .set(ReferenceDocumentStep.TO_TREAT.name())
		            .set(author.id())
		            .set(java.sql.Date.valueOf(LocalDate.now()))
		            .set(author.id())
		            .insert(new SingleOutcome<>(Long.class))
	        );
			document.updateState();
			return document;
	    } catch (SQLException ex) {
	        throw new DatabaseException(ex);
	    }
	}
	
	@Override
	public Long count() {
		return this.origin.count();
	}

	@Override
	public Double totalAmount() {
		return this.origin.totalAmount();
	}
	
	@Override
	public Double amountLeft() {
		return this.origin.amountLeft();
	}

	@Override
	public boolean hasAny() {
		return this.origin.hasAny();
	}

	@Override
	public ReferenceDocument first() {
		return this.origin.first();
	}
	
	@Override
	public boolean has(final String reference, final ReferenceDocumentType type) {
		final ReferenceDocumentCriteria cr =
			new ReferenceDocumentCriteria(this.issuer, type);
		cr.reference(reference);
		final ReferenceDocuments docsoftype =
			new DbPaginedReferenceDocuments(this.source, cr);
		return docsoftype.hasAny();
	}

	@Override
	public boolean has(String otherreference) {
		final ReferenceDocumentCriteria cr =
				new ReferenceDocumentCriteria(this.issuer, ReferenceDocumentType.NONE);
			cr.otherReference(otherreference);
			final ReferenceDocuments docsoftype =
				new DbPaginedReferenceDocuments(this.source, cr);
			return docsoftype.hasAny();
	}
}
