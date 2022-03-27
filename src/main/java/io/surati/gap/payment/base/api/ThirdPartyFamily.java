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
 * ThirdPartyFamily.
 * 
 * @since 3.0
 */
public interface ThirdPartyFamily {
	
	/**
	 * Code.
	 * @return code
	 */
	Long id();
	
	/**
	 * Code.
	 * @return code
	 */
	String code();
	
	/**
	 * Name.
	 * @return name
	 */
	String name();

	/**
	 * Update.
	 * @param code Code
	 * @param name Name
	 */
	void update(String code, String name);
	
	ThirdPartyFamily EMPTY = new ThirdPartyFamily() {
		
		@Override
		public void update(String code, String name) {
			throw new UnsupportedOperationException("ThirdPartyFamily#update");
		}
		
		@Override
		public String name() {
			return StringUtils.EMPTY;
		}
		
		@Override
		public Long id() {
			return 0L;
		}
		
		@Override
		public String code() {
			return StringUtils.EMPTY;
		}
	};
}
