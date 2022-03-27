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
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.Banks;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Pagined banks from Database.
 * 
 * @since 3.0
 */
public final class DbPaginedBanks implements Banks {
	
	/**
	 * Origin.
	 */
	private final Banks origin;
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	private final Long nbperpage;
	
	private final Long page;
	
	private final String filter;

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedBanks(final DataSource source, final Long nbperpage, final Long page, final String filter) {
		this.origin = new DbBanks(source);
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.filter = filter;
	}
	
	@Override
	public Bank get(String code) {
		return this.origin.get(code);
	}
	
	@Override
	public Bank get(Long id) {
		return this.origin.get(id);
	}
	
	@Override
	public Bank add(String code, String name, String abbreviated) {
		return this.origin.add(code, name, abbreviated);
	}
	
	@Override
	public void remove(final Long id) {		
		this.origin.remove(id);
	}
	
	@Override
	public Iterable<Bank> iterate() {		
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT bk.id FROM pay_bank as bk",
                            "LEFT JOIN pay_third_party as tp ON tp.id = bk.id",
                            "LEFT JOIN ad_person as ps ON ps.id = bk.id",
            				"WHERE tp.code ILIKE ? OR tp.abbreviated ILIKE ? OR ps.name ILIKE ?",
            				"LIMIT ? OFFSET ?"
                        ).toString()
                    )
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.nbperpage)
                    .set(this.nbperpage * (this.page - 1))
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbBank(
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
	public boolean has(String code) {
		return this.origin.has(code);
	}

	@Override
	public Long count() {
		try {
			return
				new JdbcSession(this.source)
					.sql(
	                    new Joined(
	                        " ",
	                        "SELECT COUNT(bk.*) FROM pay_bank as bk",
	                        "LEFT JOIN pay_third_party as tp ON tp.id = bk.id",
	                        "LEFT JOIN ad_person as ps ON ps.id = bk.id",
	                        "WHERE tp.code ILIKE ? OR tp.abbreviated ILIKE ? OR ps.name ILIKE ?"
	                    ).toString()
	                )
					.set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
