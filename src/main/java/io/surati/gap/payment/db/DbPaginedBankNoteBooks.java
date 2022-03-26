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
package io.surati.gap.payment.db;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.BankNoteBook;
import io.surati.gap.payment.api.BankNoteBookStatus;
import io.surati.gap.payment.api.BankNoteBooks;
import org.apache.commons.lang.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Pagined bank note books from Database.
 * 
 * @since 3.0
 */
public final class DbPaginedBankNoteBooks implements BankNoteBooks {

	/**
	 * Origin.
	 */
	private final BankNoteBooks origin;
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	private final Long nbperpage;
	
	private final Long page;
	
	private final String filter;
	
	private final BankNoteBookStatus status;

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedBankNoteBooks(final DataSource source, final Long nbperpage, final Long page, final String filter, final BankNoteBookStatus status) {
		this.origin = new DbBankNoteBooks(source);
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.filter = filter;
		this.status = status;
	}
	
	public DbPaginedBankNoteBooks(final DataSource source, final BankNoteBookStatus status) {
		this(source, Long.MAX_VALUE, 1L, StringUtils.EMPTY, status);
	}

	@Override
	public Iterable<BankNoteBook> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT bnb.id FROM bank_note_book as bnb",
	                        "LEFT JOIN bank_account as ba ON ba.id = bnb.account_id",
	                        "LEFT JOIN bank as bk ON bk.id = ba.bank_id",
	                        "LEFT JOIN third_party as tp ON tp.id = bk.id",
	                        "LEFT JOIN person as ps ON ps.id = bk.id",
	                        "WHERE (ba.number ILIKE ? OR ba.branch_code ILIKE ? OR ps.name ILIKE ? OR tp.abbreviated ILIKE ?)",
	                        "AND (bnb.status_id = ? OR ? = 'NONE')",
            				"LIMIT ? OFFSET ?"
                        ).toString()
                    )
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.status.name())
                    .set(this.status.name())
                    .set(this.nbperpage)
                    .set(this.nbperpage * (this.page - 1))
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBankNoteBook(
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
	public Iterable<BankNoteBook> iterate(BankNoteBookStatus status) {
		return this.origin.iterate(status);
	}

	@Override
	public BankNoteBook get(Long id) {
		return this.origin.get(id);
	}

	@Override
	public void remove(Long id) {
		this.origin.remove(id);
	}
	
	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql(
	                    new Joined(
	                        " ",
	                        "SELECT COUNT(bnb.*) FROM bank_note_book as bnb",
	                        "LEFT JOIN bank_account as ba ON ba.id = bnb.account_id",
	                        "LEFT JOIN bank as bk ON bk.id = ba.bank_id",
	                        "LEFT JOIN third_party as tp ON tp.id = bk.id",
	                        "LEFT JOIN person as ps ON ps.id = bk.id",
	                        "WHERE (ba.number ILIKE ? OR ba.branch_code ILIKE ? OR ps.name ILIKE ? OR tp.abbreviated ILIKE ?)",
	                        "AND (bnb.status_id = ? OR ? = 'NONE')"
	                    ).toString()
	                )
					.set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.status.name())
                    .set(this.status.name())
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
