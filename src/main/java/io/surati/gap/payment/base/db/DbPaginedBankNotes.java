/**
MIT License

Copyright (c) 2021 Surati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */  
package io.surati.gap.payment.base.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.commons.utils.time.Period;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNotes;
import io.surati.gap.payment.base.api.PaymentStatus;
import java.time.LocalDate;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Pagined bank notes from Database.
 * 
 * @since 3.0
 */
public final class DbPaginedBankNotes implements BankNotes {
	
	/**
	 * Origin.
	 */
	private final BankNotes origin;
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	private final Long nbperpage;
	
	private final Long page;
	
	private final String filter;

	private final Period payperiod;

	private final PaymentStatus status;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedBankNotes(final DataSource source, final Long nbperpage, final Long page, final String filter, final Period payperiod, final PaymentStatus status) {
		this.origin = new DbBankNotes(source);
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.filter = filter;
		this.payperiod = payperiod;
		this.status = status;
	}
	
	@Override
	public Iterable<BankNote> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT bn.id FROM pay_bank_note as bn",
							"LEFT JOIN pay_payment as pay ON pay.id = bn.id",
	                        "LEFT JOIN pay_third_party as tp ON tp.id = pay.beneficiary_id",
	                        "LEFT JOIN ad_person as ps ON ps.id = tp.id",
	                        "LEFT JOIN pay_bank_note_book as bnb ON bnb.id = bn.book_id",
	                        "WHERE (pay.issuer_reference ILIKE ? OR pay.internal_reference ILIKE ? OR pay.edition_place ILIKE ? OR ps.name ILIKE ? OR tp.abbreviated ILIKE ?)",
	                        "AND (pay.status_id = ? OR ? = 'NONE')",
	                        "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR pay.date >= ?)",
	                        "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR pay.date <= ?)",
	                        "ORDER BY id DESC",
            				"LIMIT ? OFFSET ?"
                        ).toString()
                    )
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.status.name())
                    .set(this.status.name())
                    .set(java.sql.Date.valueOf(this.payperiod.begin() == LocalDate.MIN ? LocalDate.of(1970, 01, 01) : this.payperiod.begin()))
                    .set(java.sql.Date.valueOf(this.payperiod.begin() == LocalDate.MIN ? LocalDate.of(1970, 01, 01) : this.payperiod.begin()))
                    .set(java.sql.Date.valueOf(this.payperiod.end() == LocalDate.MAX ? LocalDate.of(1970, 01, 01) : this.payperiod.end()))
                    .set(java.sql.Date.valueOf(this.payperiod.end() == LocalDate.MAX ? LocalDate.of(1970, 01, 01) : this.payperiod.end()))
                    .set(this.nbperpage)
                    .set(this.nbperpage * (this.page - 1))
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNote(
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
	public Iterable<BankNote> iterate(PaymentStatus status) {
		return this.origin.iterate(status);
	}

	@Override
	public BankNote get(Long id) {
		return this.origin.get(id);
	}
	
	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql(
	                    new Joined(
	                        " ",
	                        "SELECT COUNT(bn.*) FROM pay_bank_note as bn",
							"LEFT JOIN pay_payment as pay ON pay.id = bn.id",
	                        "LEFT JOIN pay_third_party as tp ON tp.id = pay.beneficiary_id",
	                        "LEFT JOIN ad_person as ps ON ps.id = tp.id",
	                        "LEFT JOIN pay_bank_note_book as bnb ON bnb.id = bn.book_id",
	                        "WHERE (pay.issuer_reference ILIKE ? OR pay.internal_reference ILIKE ? OR pay.edition_place ILIKE ? OR ps.name ILIKE ? OR tp.abbreviated ILIKE ?)",
	                        "AND (pay.status_id = ? OR ? = 'NONE')",
	                        "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR pay.date >= ?)",
	                        "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR pay.date <= ?)"
	                    ).toString()
	                )
					.set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.status.name())
                    .set(this.status.name())
                    .set(java.sql.Date.valueOf(this.payperiod.begin() == LocalDate.MIN ? LocalDate.of(1970, 01, 01) : this.payperiod.begin()))
                    .set(java.sql.Date.valueOf(this.payperiod.begin() == LocalDate.MIN ? LocalDate.of(1970, 01, 01) : this.payperiod.begin()))
                    .set(java.sql.Date.valueOf(this.payperiod.end() == LocalDate.MAX ? LocalDate.of(1970, 01, 01) : this.payperiod.end()))
                    .set(java.sql.Date.valueOf(this.payperiod.end() == LocalDate.MAX ? LocalDate.of(1970, 01, 01) : this.payperiod.end()))
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
