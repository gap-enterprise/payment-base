package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import io.surati.gap.admin.api.Access;
import io.surati.gap.admin.api.User;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.ReferenceDocument;
import io.surati.gap.payment.api.ReferenceDocumentStep;
import io.surati.gap.payment.api.ReferenceDocumentsToPay;
import org.cactoos.scalar.LengthOf;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class DbReferenceDocumentsToPay implements ReferenceDocumentsToPay {

	private final DataSource source;

	private final User user;

	DbReferenceDocumentsToPay(final DataSource source, final User user) {
		this.source = source;
		this.user = user;
	}

	@Override
	public ReferenceDocument get(Long id) {
		for (ReferenceDocument doc : this.iterate()) {
			if(doc.id().equals(id)) {
				return doc;
			}
		}
		throw new IllegalArgumentException("Le document de référence n'a pas été retrouvé !"); 
	}

	@Override
	public Iterable<ReferenceDocument> iterate() {
		final boolean canviewall = false;
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT id FROM reference_document",
                            "WHERE step_id=? AND (?=true OR worker_id=?)",
            				"ORDER BY date ASC"
                        ).toString()
                    )
                    .set(ReferenceDocumentStep.SELECTED.name())
                    .set(canviewall)
                    .set(user.id())
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
	public void select(final ReferenceDocument document) {
		if(document.step() != ReferenceDocumentStep.TO_TREAT) {
			throw new IllegalArgumentException("Vous ne pouvez pas sélectionnée ce document !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE reference_document",
                        "SET step_id=?, worker_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(ReferenceDocumentStep.SELECTED.name())
                .set(user.id())
                .set(document.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}
	
	@Override
	public void deselect(final ReferenceDocument document) {
		if(document.step() != ReferenceDocumentStep.SELECTED) {
			throw new IllegalArgumentException("Vous ne pouvez pas déslectionnée ce document !");
		}
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE reference_document",
                        "SET step_id=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(ReferenceDocumentStep.TO_TREAT.name())
                .set(document.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public Double amountToPay() {
		Double amount = 0.0;
		for (ReferenceDocument doc : this.iterate()) {
			amount += doc.amountLeft();
		}
		return amount;
	}

	@Override
	public void select(Iterable<ReferenceDocument> documents) {
		try {
			if(new LengthOf(documents).value() > 200) {
				throw new IllegalArgumentException("Vous ne pouvez pas ajouter plus de 200 éléments à la fois !");
			}
			for (ReferenceDocument document : documents) {
				this.select(document);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
