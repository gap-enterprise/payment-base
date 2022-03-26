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

import org.apache.commons.lang3.StringUtils;

/**
 * Bank.
 * 
 * @since 3.0
 */
public interface Bank extends ThirdParty {
	
	String representative();
	
	String representativePosition();
	
	String representativeCivility();
	
	String headquarters();
	
	PaymentMeans paymentMeans();
	
	void headquarters(String place);

	void representative(String name, String position, String civility);
	
	Bank EMPTY = new Bank() {
		
		@Override
		public void update(String name) {
			throw new UnsupportedOperationException("Bank#update");
		}
		
		@Override
		public String name() {
			return "Aucune";
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public void update(String code, String name, String abbreviated) {
			throw new UnsupportedOperationException("Bank#update");
		}
		
		@Override
		public ThirdPartyFamily family() {
			return ThirdPartyFamily.EMPTY;
		}
		
		@Override
		public String code() {
			return StringUtils.EMPTY;
		}
		
		@Override
		public void assign(ThirdPartyFamily family) {
			throw new UnsupportedOperationException("Bank#assign");
		}
		
		@Override
		public String abbreviated() {
			return "Aucune";
		}
		
		@Override
		public String representativePosition() {
			return StringUtils.EMPTY;
		}
		
		@Override
		public String representativeCivility() {
			return StringUtils.EMPTY;
		}
		
		@Override
		public void representative(String name, String position, String civility) {
			throw new UnsupportedOperationException("Bank#representative");
		}
		
		@Override
		public String representative() {
			return StringUtils.EMPTY;
		}
		
		@Override
		public PaymentMeans paymentMeans() {
			throw new UnsupportedOperationException("Bank#paymentMeans");
		}
		
		@Override
		public void headquarters(String place) {
			throw new UnsupportedOperationException("Bank#headquarters");
		}
		
		@Override
		public String headquarters() {
			return StringUtils.EMPTY;
		}

		@Override
		public PaymentCondition paymentCondition() {
			
			return null;
		}
	};
}
