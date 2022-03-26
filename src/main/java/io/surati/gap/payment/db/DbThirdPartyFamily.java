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

import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.ThirdPartyFamily;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Third Party Family in Database.
 * 
 * @since 3.0
 */
public final class DbThirdPartyFamily implements ThirdPartyFamily {

	/**
	 * Data source.
	 */
	private final DataSource source;
	
	/**
	 * ID.
	 */
	private final Long id;
	
	/**
	 * Ctor.
	 * @param source Data source
	 * @param id Identifier
	 */
	public DbThirdPartyFamily(final DataSource source, final Long id) {
		this.source = source;
		this.id = id;
	}
	
	/**
	 * Checks if a Third Party Family with code and not id exists
	 * @param code
	 * @param id
	 * @return boolean exits
	 */
	private boolean codeIsUsed(String code) {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) as nb FROM third_party_family WHERE code=? AND id <>?")
			){
				pstmt.setString(1, code);
				pstmt.setLong(2, id);
			
				try(final ResultSet rs = pstmt.executeQuery()){
					rs.next();
					Long nb = rs.getLong(1);
					return nb > 0;
				}
			} catch(SQLException e) {
				throw new DatabaseException(e);
			}
	}
	
	@Override
	public Long id() {
		return this.id;
	}

	@Override
	public String code() {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("SELECT code FROM third_party_family WHERE id=?")
			){
				pstmt.setLong(1, id);
				try (final ResultSet rs = pstmt.executeQuery()) {
					rs.next();
					return rs.getString(1);
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
	}
	
	@Override
	public String name() {
		try (
				final Connection connection = source.getConnection();
				final PreparedStatement pstmt = connection.prepareStatement("SELECT name FROM third_party_family WHERE id=?")
			){
				pstmt.setLong(1, id);
				try (final ResultSet rs = pstmt.executeQuery()) {
					rs.next();
					return rs.getString(1);
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
	}
	
	@Override
	public void update(final String code, final String name) {
		
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("L'intitulé doit être renseigné !");
		
		if(code == null || code.trim().isEmpty())
			throw new IllegalArgumentException("Le code doit être renseigné !");

		if(this.codeIsUsed(code))
			throw new IllegalArgumentException("Ce code est déjà utilisé.");
			
		try (
			final Connection connection = source.getConnection();
				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE third_party_family SET code=?, name=? WHERE id=?")
		) {
			pstmt.setString(1, code);
			pstmt.setString(2, name);
			pstmt.setLong(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public String toString() {
		return String.format("Code=%s, Intitulé=%s", this.code(), this.name());
	}
}
