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
import io.surati.gap.payment.api.ThirdParties;
import io.surati.gap.payment.api.ThirdParty;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Pagined Third parties from Database.
 * 
 * @since 3.0
 */

public final class DbPaginedThirdParties implements ThirdParties {
	
	/**
	 * Origin.
	 */
	private final ThirdParties origin;
	
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
	public DbPaginedThirdParties(final DataSource source, final Long nbperpage, final Long page, final String filter) {
		this.origin = new DbThirdParties(source);
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.filter = filter;
	}

	@Override
	public boolean has(String code) {
		return this.origin.has(code);
	}

	@Override
	public ThirdParty get(String code) {
		return this.origin.get(code);
	}

	@Override
	public ThirdParty get(Long id) {
		return this.origin.get(id);
	}

	@Override
	public ThirdParty add(String code, String name, String abbreviated) {
		return this.origin.add(code, name, abbreviated);
	}

	@Override
	public void remove(Long id) {
		this.origin.remove(id);
	}

	@Override
	public Iterable<ThirdParty> iterate() {
		try {
            return 
                new JdbcSession(this.source)
                    .sql(
                        new Joined(
                            " ",
                            "SELECT tp.id FROM third_party as tp",
	                        "LEFT JOIN person as ps ON ps.id = tp.id",
	                        "LEFT JOIN third_party_family as tf ON tf.id = tp.family_id",
                            "WHERE tp.code ILIKE ? OR tp.abbreviated ILIKE ? OR ps.name ILIKE ? OR tf.name ILIKE ? OR tf.code ILIKE ?",
                            "ORDER BY tp.abbreviated ASC",
            				"LIMIT ? OFFSET ?"
                        ).toString()
                    )
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set("%" + this.filter + "%")
                    .set(this.nbperpage)
                    .set(this.nbperpage * (this.page - 1))
                    .select(
                        new ListOutcome<>(
                            rset ->
                            new DbThirdParty(
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
			return
				new JdbcSession(this.source)
					.sql(
	                    new Joined(
	                        " ",
	                        "SELECT COUNT(tp.*) FROM third_party as tp",
	                        "LEFT JOIN person as ps ON ps.id = tp.id",
	                        "LEFT JOIN third_party_family as tf ON tf.id = tp.family_id",
                            "WHERE tp.code ILIKE ? OR tp.abbreviated ILIKE ? OR ps.name ILIKE ? OR tf.name ILIKE ? OR tf.code ILIKE ?"
	                    ).toString()
	                )
					.set("%" + this.filter + "%")
					.set("%" + this.filter + "%")
					.set("%" + this.filter + "%")
					.set("%" + this.filter + "%")
					.set("%" + this.filter + "%")
					.select(new SingleOutcome<>(Long.class));
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
