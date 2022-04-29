package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.commons.utils.convert.filter.FilterSQLPrinter;
import io.surati.gap.commons.utils.convert.filter.Interval;
import io.surati.gap.commons.utils.convert.filter.IntervalSQLPrinter;
import io.surati.gap.commons.utils.convert.filter.SQLPrinter;
import io.surati.gap.commons.utils.convert.filter.Sorter;
import io.surati.gap.commons.utils.convert.filter.SorterSQLPrinter;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ReferenceDocuments;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.filter.ReferenceDocumentCriteria;
import org.apache.commons.lang.StringUtils;
import org.cactoos.list.ListOf;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public final class DbPaginedReferenceDocuments implements ReferenceDocuments {
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	private final Long nbperpage;
	
	/**
	 * Page number to show
	 * <p>Always starts at 1
	 */
	private final Long page;
	
	private final ReferenceDocumentCriteria criteria;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedReferenceDocuments(final DataSource source) {
		this(source, new ReferenceDocumentCriteria());
	}

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedReferenceDocuments(final DataSource source, final ReferenceDocumentCriteria criteria) {
		this(source, Long.MAX_VALUE, 1L, criteria);
	}

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedReferenceDocuments(final DataSource source, final Long nbperpage, final Long page, final ReferenceDocumentCriteria criteria) {
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.criteria = criteria;
	}
	
	@Override
	public Iterable<ReferenceDocument> iterate() {
		try {
            return this.session(
            	"SELECT id",
            	StringUtils.EMPTY,
            	new Joined(
                    " ",
                    this.sorterSql(),
    				"LIMIT ? OFFSET ?"
                ).toString()
            )
            .set(this.nbperpage)
            .set(this.nbperpage * (this.page - 1))
            .select(
                new ListOutcome<>(
                    rset ->
                    new DbReferenceDocument(
                        this.source,
                        rset.getLong(1)
                    )
                )
            );
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Long count() {
		try {
			return this.session("SELECT COUNT(*)")
				.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public ReferenceDocument get(Long id) {
		if(this.has(id)) {
			return new DbReferenceDocument(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("Le document (ID=%s) n'a pas été trouvé !", id));
		}
	}

	@Override
	public void remove(ReferenceDocument item) {
		if(!this.has(item.id())) {
			throw new IllegalArgumentException(String.format("Le document (N°%s) ne fait pas partie de la sélection courante !", item.reference()));
		}
		final ReferenceDocumentStatus status = item.status();
		if(status != ReferenceDocumentStatus.WAITING_FOR_PAYMENT) {
			throw new IllegalArgumentException(String.format("Le document de référence (N°%s) doit avoir le statut %s avant d'être supprimé !", item.reference(), status));
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "DELETE FROM pay_reference_document",
                        "WHERE id=?"
                    ).toString()
                )
                .set(item.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Double totalAmount() {
		try {
			return
				this.session("SELECT SUM(amount)")
					.select(new SingleOutcome<>(Long.class)).doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
	
	@Override
	public Double amountLeft() {
		try {
			return
				this.session("SELECT SUM(amount_left)")
					.select(new SingleOutcome<>(Long.class)).doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public boolean hasAny() {
		return this.count() > 0L;
	}

	@Override
	public ReferenceDocument first() {
		if(this.hasAny()) {
			Iterator<ReferenceDocument> it = this.iterate().iterator();
			return it.next();
		} else {
			throw new IllegalArgumentException("La sélection ne contient pas de document !");
		}
	}
	
	private boolean has(final Long id) {
		try {
			return this.session("SELECT COUNT(*)", "id=?")
			    .set(id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	private JdbcSession session(final String select) {
		return this.session(select, StringUtils.EMPTY);
	}

	private JdbcSession session(final String select, final String filter) {
		return this.session(select, filter, StringUtils.EMPTY);
	}

	private JdbcSession session(final String select, final String filter, final String limit) {
		final StringBuilder sql = new StringBuilder(
			new Joined(
	            " ",
	            select,
	            "FROM pay_reference_document_view",
	            "WHERE 1=1"
	        ).toString()
		);
		final Collection<Object> args = new LinkedList<>();
		final ReferenceDocumentType type = this.criteria.type();
		if(type != ReferenceDocumentType.NONE) {
			sql.append(" ")
				.append("AND type_id=?");
			args.add(type.name());
		}
		final ReferenceDocumentStep step = this.criteria.step();
		if(step != ReferenceDocumentStep.NONE) {
			sql.append(" ")
				.append("AND step_id=?");
			args.add(step.name());
		}
		final StringBuilder sqlstatus = new StringBuilder();
		for (ReferenceDocumentStatus status : this.criteria.statusAvailables()) {
			if(StringUtils.isBlank(sqlstatus.toString())) {
				sqlstatus.append("status_id=?");
			} else {
				sqlstatus.append(" ").append("OR status_id=?");
			}
			args.add(status.name());
		}
		if(StringUtils.isNotBlank(sqlstatus.toString())) {
			sql.append(" ")
				.append(
					String.format("AND (%s)", sqlstatus)
				);
		}
		final ThirdParty issuer = this.criteria.issuer();
		if(issuer != ThirdParty.EMPTY) {
			sql.append(" AND beneficiary_id=?");
			args.add(issuer.id());
		}
		if(StringUtils.isNotBlank(this.criteria.reference())) {
			sql.append(" AND reference=?");
			args.add(this.criteria.reference());
		}
		if(StringUtils.isNotBlank(this.criteria.otherReference())) {
			sql.append(" AND internal_reference=?");
			args.add(this.criteria.otherReference());
		}
		final SQLPrinter filterprint = new FilterSQLPrinter(
			this.criteria.filters(),
			"beneficiary_name ILIKE ? OR beneficiary_abbreviated ILIKE ? OR reference ILIKE ? OR place ILIKE ? OR object ILIKE ?"
		);
		sql.append(filterprint);
		args.addAll(new ListOf<>(filterprint.args()));
		final Map<String, Interval> intervalmap = new LinkedHashMap<>();
		for (Interval interval : this.criteria.intervals()) {
			if(interval.field() == ReferenceDocument.DATE) {
				intervalmap.put("date", interval);
			}
		}
		final SQLPrinter intervalprinter = new IntervalSQLPrinter(intervalmap);
		sql.append(intervalprinter);
		args.addAll(new ListOf<>(intervalprinter.args()));
		if(StringUtils.isNotBlank(filter)) {
			sql.append(" AND ").append(filter);
		}
		if(StringUtils.isNotBlank(limit)) {
			sql.append(" ").append(limit);
		}
		final JdbcSession session =
			new JdbcSession(this.source).sql(sql.toString());
		for (Object arg : args) {
			session.set(arg);
		}
		return session;
	}
	
	private String sorterSql() {
		final Map<String, Sorter> sortermap = new LinkedHashMap<>();
		for (Sorter sorter : this.criteria.sorters()) {
			if(sorter.field() == ReferenceDocument.DATE) {
				sortermap.put("date", sorter);
			}
			if(sorter.field() == ReferenceDocument.AMOUNT) {
				sortermap.put("amount", sorter);
			}
			if(sorter.field() == ReferenceDocument.BENEFICIARY) {
				sortermap.put("beneficiary_name", sorter);
			}
		}
		final SQLPrinter sorterprinter = new SorterSQLPrinter(sortermap);
		return sorterprinter.toString();
	}
}
