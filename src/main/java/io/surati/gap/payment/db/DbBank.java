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
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Bank;
import io.surati.gap.payment.api.PaymentCondition;
import io.surati.gap.payment.api.PaymentMeans;
import io.surati.gap.payment.api.ThirdParty;
import io.surati.gap.payment.api.ThirdPartyFamily;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * User in Database.
 * 
 * @since 3.0
 */
public final class DbBank implements Bank {

	private final ThirdParty tp;
	
	private final DataSource source;
	
	public DbBank(final DataSource source, final Long id) {
		this(source, new DbThirdParty(source, id));
	}

	public DbBank(final DataSource source, final ThirdParty tp) {
		this.source = source;
		this.tp = tp;
	}
	
	@Override
	public String code() {
		return this.tp.code();
	}

	@Override
	public String abbreviated() {
		return this.tp.abbreviated();
	}

	@Override
	public void update(String code, String name, String abbreviated) {
		this.tp.update(code, name, abbreviated);
	}

	@Override
	public Long id() {
		return this.tp.id();
	}

	@Override
	public String name() {
		return this.tp.name();
	}

	@Override
	public void update(String name) {
		this.tp.update(name);
	}

	@Override
	public PaymentMeans paymentMeans() {
		return new DbPaymentMeans(this.source, this);
	}

	@Override
	public String representative() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT representative FROM bank",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.tp.id())
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String headquarters() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT headquarters FROM bank",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.tp.id())
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String representativePosition() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT representative_position FROM bank",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.tp.id())
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public String representativeCivility() {
		try {
			return new JdbcSession(this.source)
				.sql(
	        		new Joined(
        				" ",
        				"SELECT representative_civility FROM bank",
        				"WHERE id=?"
        			).toString()
        		)
				.set(this.tp.id())
	            .select(new SingleOutcome<>(String.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void headquarters(String place) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE bank",
                        "SET headquarters=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(place)
                .set(this.tp.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public void representative(String name, String position, String civility) {
		try {
            new JdbcSession(this.source)
                .sql(
                    new Joined(
                        " ",
                        "UPDATE bank",
                        "SET representative=?,representative_position=?,representative_civility=?",
                        "WHERE id=?"
                    ).toString()
                )
                .set(name)
                .set(position)
                .set(civility)
                .set(this.tp.id())
                .execute();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
	}

	@Override
	public ThirdPartyFamily family() {
		return this.tp.family();
	}

	@Override
	public void assign(ThirdPartyFamily family) {
		this.tp.assign(family);
	}

	@Override
	public PaymentCondition paymentCondition() {
		return this.tp.paymentCondition();
	}
	
	@Override
	public String toString() {
		return String.format("Code=%s, Intitulé=%s", this.code(), this.name());
	}
}
