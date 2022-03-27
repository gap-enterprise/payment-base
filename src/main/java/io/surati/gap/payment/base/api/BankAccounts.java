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
package io.surati.gap.payment.base.api;


/**
 * List of Bank accounts.
 * 
 * @since 3.0
 */
public interface BankAccounts {

	/**
	 * Checks if account of full number (24 digits) exists.
	 * @param fullnumber Full number
	 * @return boolean exits
	 * @throws IllegalArgumentException If not found or fullnumber doesn't match
	 */
	boolean has(String fullnumber);
	
	/**
	 * Get account by full number (24 digits).
	 * @param fullnumber Full number
	 * @return Account
	 * @throws IllegalArgumentException If not found
	 */
	BankAccount get(String fullnumber);
	
	/**
	 * Get account by ID.
	 * @param id Identifier
	 * @return Account
	 * @throws IllegalArgumentException If not found
	 */
	BankAccount get(Long id);
	
	/**
	 * Add a new account.
	 * @param bank Bank associated
	 * @param branchcode Branch code
	 * @param number Number
	 * @param key Key
	 */
	BankAccount add(Bank bank, String branchcode, String number, String key);
	
	/**
	 * Remove an account.
	 * @param id Identifier
	 */
	void remove(Long id);
	
	/**
	 * Iterate all accounts.
	 * @return All accounts
	 */
	Iterable<BankAccount> iterate();
}
