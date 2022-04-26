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

import io.surati.gap.admin.base.api.Person;

/**
 * Third party.
 * 
 * @since 3.0
 */
public interface ThirdParty extends Person {
	
	/**
	 * Code.
	 * @return code
	 */
	String code();
	
	/**
	 * Abbreviated.
	 * @return abbreviated
	 */
	String abbreviated();
	
	/**
	 * Update.
	 * @param code Code
	 * @param name Name
	 * @param abbreviated Abbreviated
	 */
	void update(String code, String name, String abbreviated);
	
	/**
	 * Family (If haven't family, get EMPTY).
	 * @return family
	 */
	ThirdPartyFamily family();
	
	/**
	 * Assign a family.
	 * @param family Family to assign
	 */
	void assign(ThirdPartyFamily family);
	
	PaymentCondition paymentCondition();
	
	ThirdParty EMPTY = new ThirdParty() {
		
		@Override
		public void update(String name) {
			
			
		}

		@Override
		public void contacts(String s, String s1, String s2, String s3) {

		}

		@Override
		public String name() {
			
			return null;
		}

		@Override
		public String address() {
			return null;
		}

		@Override
		public String pobox() {
			return null;
		}

		@Override
		public String phone() {
			return null;
		}

		@Override
		public String email() {
			return null;
		}

		@Override
		public Long id() {
			
			return null;
		}
		
		@Override
		public void update(String code, String name, String abbreviated) {
			
			
		}
		
		@Override
		public PaymentCondition paymentCondition() {
			
			return null;
		}
		
		@Override
		public ThirdPartyFamily family() {
			
			return null;
		}
		
		@Override
		public String code() {
			
			return null;
		}
		
		@Override
		public void assign(ThirdPartyFamily family) {
			
			
		}
		
		@Override
		public String abbreviated() {
			
			return null;
		}
	};
}
