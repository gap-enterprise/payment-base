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
import io.surati.gap.commons.utils.time.Period;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.PaymentOrder;
import io.surati.gap.payment.api.PaymentOrderStatus;
import io.surati.gap.payment.api.PaymentOrders;
import org.apache.commons.lang.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Pagined payment orders from Database.
 * 
 * @since 3.0
 */

public final class DbPaginedPaymentOrders implements PaymentOrders {
	
	/**
	 * Data Source.
	 */
	private final DataSource source;
	
	private final Long nbperpage;
	
	private final Long page;
	
	private final String filter;
	
	private final Period opperiod;
	
	private final Period refdocperiod;

	private final PaymentOrderStatus status;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedPaymentOrders(final DataSource source) {
		this(source, Period.EMPTY, Period.EMPTY, PaymentOrderStatus.NONE);
	}

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedPaymentOrders(final DataSource source, final PaymentOrderStatus status) {
		this(source, Period.EMPTY, Period.EMPTY, status);
	}

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedPaymentOrders(final DataSource source, final Period opperiod, final Period refdocperiod, final PaymentOrderStatus status) {
		this(source, Long.MAX_VALUE, 1L, StringUtils.EMPTY, opperiod, refdocperiod, status);
	}

	/**
	 * Ctor.
	 * @param source
	 */
	public DbPaginedPaymentOrders(final DataSource source, final Long nbperpage, final Long page, final String filter, final Period opperiod, final Period refdocperiod, final PaymentOrderStatus status) {
		this.source = source;
		this.nbperpage = nbperpage;
		this.page = page;
		this.filter = filter;
		this.opperiod = opperiod;
		this.refdocperiod = refdocperiod;
		this.status = status;
	}
	
	@Override
	public Iterable<PaymentOrder> iterate() {
		try {
			return this.session(
				"SELECT po.id",
				StringUtils.EMPTY,
				new Joined(
                    " ",
                    StringUtils.EMPTY,
                    "ORDER BY po.id DESC",
    				"LIMIT ? OFFSET ?"
                ).toString()
			)
			.set(this.nbperpage)
        	.set(this.nbperpage * (this.page - 1))
			.select(
                new ListOutcome<>(
                    rset ->
                    new DbPaymentOrder(
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
	public PaymentOrder get(Long id) {
		if(this.has(id)) {
			return new DbPaymentOrder(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement avec ID %s n'a pas été trouvé !", id));
		}
	}

	@Override
	public void remove(final PaymentOrder item) {
		final PaymentOrders tporders = new DbThirdPartyPaymentOrders(this.source, item.beneficiary());
		tporders.remove(item);
	}

	@Override
	public Long count() {
		try {
			return
				this.session("SELECT COUNT(po.*)")
					.select(new SingleOutcome<>(Long.class));
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	private boolean has(final Long id) {
		try {
			return 
				this.session("SELECT COUNT(po.*)", "po.id=?")
				    .set(id)
					.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(String reference) {
		try {
			return
				this.session("SELECT COUNT(po.*)", "po.reference=?")
				    .set(reference)
					.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public Double totalAmount() {
		try {
			return
				this.session("SELECT SUM(po.amount_to_pay)")
					.select(new SingleOutcome<>(Long.class)).doubleValue();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public PaymentOrder get(String reference) {
		if(this.has(reference)) {
			try {
				return new DbPaymentOrder(
					this.source,
					this.session("SELECT po.id", "po.reference=?")
					    .set(reference)
						.select(new SingleOutcome<>(Long.class))
				);
			} catch(SQLException ex) {
				throw new DatabaseException(ex);
			}
		} else {
			throw new IllegalArgumentException(String.format("L'ordre de paiement avec la référence %s n'a pas été trouvé !", reference));
		}
	}

	private JdbcSession session(final String select) {
		return this.session(select, StringUtils.EMPTY);
	}

	private JdbcSession session(final String select, final String filter) {
		return this.session(select, filter, StringUtils.EMPTY);
	}

	private JdbcSession session(final String select, final String filter, final String order) {
		final String sql = new Joined(
            " ",
            select,
            "FROM pay_payment_order as po",
            "LEFT JOIN pay_third_party as tp ON tp.id = po.beneficiary_id",
            "LEFT JOIN ad_person as ps ON ps.id = po.beneficiary_id",
            "LEFT JOIN pay_reference_document as rd ON rd.id = po.reference_document_id",
            "WHERE (po.reference ILIKE ? OR rd.reference ILIKE ? OR ps.name ILIKE ? OR tp.abbreviated ILIKE ?)",
            "AND (po.status_id = ? OR ? = 'NONE')",
            "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR po.date >= ?)",
            "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR po.date <= ?)",
            "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR rd.date >= ?)",
            "AND (to_char(?::date, 'YYYY-MM-DD') = '1970-01-01' OR rd.date <= ?)",
            StringUtils.isBlank(filter) ? StringUtils.EMPTY : String.format("AND (%s)", filter),
            order	
        ).toString();
		return new JdbcSession(this.source)
			.sql(sql)
			.set("%" + this.filter + "%")
			.set("%" + this.filter + "%")
			.set("%" + this.filter + "%")
			.set("%" + this.filter + "%")
            .set(this.status.name())
            .set(this.status.name())
            .set(java.sql.Date.valueOf(this.opperiod.begin()))
            .set(java.sql.Date.valueOf(this.opperiod.begin()))
            .set(java.sql.Date.valueOf(this.opperiod.end()))
            .set(java.sql.Date.valueOf(this.opperiod.end()))
            .set(java.sql.Date.valueOf(this.refdocperiod.begin()))
            .set(java.sql.Date.valueOf(this.refdocperiod.begin()))
            .set(java.sql.Date.valueOf(this.refdocperiod.end()))
            .set(java.sql.Date.valueOf(this.refdocperiod.end()));
	}
}
