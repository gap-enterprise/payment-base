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
package io.surati.gap.payment.api;

import io.surati.gap.admin.api.Person;

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
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long id() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void update(String code, String name, String abbreviated) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public PaymentCondition paymentCondition() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ThirdPartyFamily family() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String code() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void assign(ThirdPartyFamily family) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String abbreviated() {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
