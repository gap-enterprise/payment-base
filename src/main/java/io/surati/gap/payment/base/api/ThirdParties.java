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
 * List of third parties.
 * 
 * @since 3.0
 */
public interface ThirdParties {
		
	/**
	 * Checks if a third party with a code exists.
	 * @param code
	 * @return boolean exits
	 */
	boolean has(String code);
	
	/**
	 * Get third party.
	 * @param code Code
	 * @return Third party found
	 * @throws IllegalArgumentException If not found
	 */
	ThirdParty get(String code);
	
	/**
	 * Get an item.
	 * @param id Identifier
	 * @return Third party found
	 * @throws IllegalArgumentException If not found
	 */
	ThirdParty get(Long id);
	
	/**
	 * Add a new item.
	 * @param code Code
	 * @param name Name
	 * @param abbreviated Abbreviated
	 * @return Third party created.
	 */
	ThirdParty add(String code, String name, String abbreviated);
	
	/**
	 * Remove a third party by its identifier.
	 * @param id Identifier
	 */
	void remove(Long id);
	
	/**
	 * Iterate all third parties.
	 * @return Iterable of third party.
	 */
	Iterable<ThirdParty> iterate();

	Long count();
}
