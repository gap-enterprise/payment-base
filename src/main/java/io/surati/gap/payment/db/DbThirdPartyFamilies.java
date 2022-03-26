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
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.ThirdPartyFamilies;
import io.surati.gap.payment.api.ThirdPartyFamily;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.text.Joined;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Third Party Family in Database.
 * 
 * @since 3.0
 */
public final class DbThirdPartyFamilies implements ThirdPartyFamilies {

	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source
	 */
	public DbThirdPartyFamilies(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public boolean has(final String code) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM third_party_family WHERE code=?")
				.set(code)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public boolean has(final Long id) {
		try {
			return new JdbcSession(this.source)
				.sql("SELECT COUNT(*) FROM third_party_family WHERE id=?")
				.set(id)
				.select(new SingleOutcome<>(Long.class)) > 0;
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
	

	
	@Override
	public ThirdPartyFamily get(Long id) {
		if(this.has(id)) {
			return new DbThirdPartyFamily(this.source, id);
		} else {
			throw new IllegalArgumentException(String.format("La famille de tiers avec ID %s n'a pas été trouvé !", id));
		}
	}
	
	@Override
	public ThirdPartyFamily add(final String code, final String name) {		
		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Le nom ne peut être vide !");
		}
		if(StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("Le code ne peut être vide !");
		}
		if(this.has(code)) {
			throw new IllegalArgumentException("Le code est déjà utilisé !");
		}
		try {
			return new DbThirdPartyFamily(
				this.source,
				new JdbcSession(this.source)
					.sql(
						new Joined(
							" ",
                            "INSERT INTO third_party_family",
                            "(code, name)",
                            "VALUES",
                            "(?, ?)"
						).asString()
					)
					.set(code)
					.set(name)
					.insert(Outcome.LAST_INSERT_ID)
			);
		} catch(SQLException | IOException ex) {
			throw new DatabaseException(ex);
		}
	}
	
	@Override
	public Iterable<ThirdPartyFamily> iterate() {		
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM third_party_family")
		){
			final Collection<ThirdPartyFamily> items = new ArrayList<>();
			
			try(final ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					items.add(new DbThirdPartyFamily(source, rs.getLong(1)));
				}
			}
			
			return items;
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void remove(final Long id) {
		try {
			final boolean used = new JdbcSession(this.source)
		            .sql(
		                new Joined(
		                    " ",
		                    "SELECT COUNT(*) FROM third_party_family",
		                    "WHERE id=? AND id IN (SELECT family_id FROM third_party)"
		                ).toString()
		            )
		            .set(id)
		            .select(new SingleOutcome<>(Long.class)) > 0;
		        if(used) {
		        	throw new IllegalArgumentException("La famille de tiers ne peut pas être supprimée (déjà utilisée) !");
		        }
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt = connection.prepareStatement("DELETE FROM third_party_family WHERE id=?");
		) {
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public ThirdPartyFamily get(String code) {
		try {
			final Long familyid = new JdbcSession(this.source)
				.sql("SELECT id FROM third_party_family WHERE code=?")
				.set(code)
				.select(new SingleOutcome<>(Long.class));
			if(familyid.equals(0L)) {
				throw new IllegalArgumentException(String.format("La famille de tiers avec le code %s est introuvable !", code));
			} else {
				return new DbThirdPartyFamily(this.source, familyid);
			}
		} catch(SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

}
