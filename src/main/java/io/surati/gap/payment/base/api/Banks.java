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
 * List of Banks.
 * 
 * @since 3.0
 */
public interface Banks {
		
	/**
	 * Checks if a {@link Bank} with code exists.
	 * @param code
	 * @return boolean exits
	 */
	boolean has(String code);
	
	/**
	 * Get {@link Bank}.
	 * @param code
	 * @return ThirdParty
	 */
	Bank get(String code);
	
	/**
	 * Get {@link Bank}.
	 * @param id
	 * @return ThirdParty
	 */
	Bank get(Long id);
	
	/**
	 * Add a new {@link Bank}.
	 * @param code
	 * @param name
	 * @param abbreviated
	 */
	Bank add(String code, String name, String abbreviated);
	
	/**
	 * Remove a {@link Bank}.
	 * @param code
	 * @param name
	 * @param abbreviated
	 */
	void remove(Long id);
	
	/**
	 * Iterate all banks.
	 * @return All Banks
	 */
	Iterable<Bank> iterate();
	
	/**
	 * Numbers of items found
	 * @return
	 */
	Long count();
}
