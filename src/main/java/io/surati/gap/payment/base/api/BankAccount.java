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

import org.apache.commons.lang3.StringUtils;

/**
 * Bank account.
 * 
 * @since 3.0
 */
public interface BankAccount {
	
	/**
	 * Full number length.
	 */
	public static int FULL_NUMBER_LENGTH = 24;

	/**
	 * Identifier.
	 * @return id
	 */
	Long id();
	
	/**
	 * RIB.
	 * @return rib
	 */
	String rib();
	
	/**
	 * Bank.
	 * @return bank
	 */
	Bank bank();
	
	/**
	 * Branch code.
	 * @return code
	 */
	String branchCode();
	
	/**
	 * Number.
	 * @return number
	 */
	String number();
	
	/**
	 * Key.
	 * @return
	 */
	String key();
	
	BankAccountAccountingSettings accountingSettings();

	/**
	 * Update.
	 * @param branchcode Branch code
	 * @param number Number
	 * @param key Key
	 */
	void update(String branchcode, String number, String key);
	
    BankNoteBook addBook(PaymentMeanType meantype, String startnumber, String endnumber, String prefixnum);
	
	BankNoteBook addBook(PaymentMeanType meantype, String startnumber, byte leafnumber, String prefixnum);
	
	BankAccount EMPTY = new BankAccount() {
		
		@Override
		public void update(String branchcode, String number, String key) {
			throw new UnsupportedOperationException("BankAccount#update");
		}
		
		@Override
		public String rib() {
			return "Aucun";
		}
		
		@Override
		public Long id() {
			return 0L;
		}

		@Override
		public Bank bank() {
			return Bank.EMPTY;
		}

		@Override
		public String branchCode() {
			return StringUtils.EMPTY;
		}

		@Override
		public String number() {
			return StringUtils.EMPTY;
		}

		@Override
		public String key() {
			return StringUtils.EMPTY;
		}

		@Override
		public BankNoteBook addBook(PaymentMeanType meantype, String startnumber, String endnumber, String prefixnum) {
			throw new UnsupportedOperationException("BankNoteBook#addBook");
		}

		@Override
		public BankNoteBook addBook(PaymentMeanType meantype, String startnumber, byte leafnumber, String prefixnum) {
			throw new UnsupportedOperationException("BankNoteBook#addBook");
		}

		@Override
		public BankAccountAccountingSettings accountingSettings() {
			
			return null;
		}
	};
}
