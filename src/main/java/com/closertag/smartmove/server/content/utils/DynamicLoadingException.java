package com.closertag.smartmove.server.content.utils;

/**
 * @author Clint Gilbert
 * 
 *         Apr 9, 2007
 * 
 *         Childrens Hospital Informatics Program at the Harvard-MIT division of
 *         Health Sciences Technology
 * @link http://www.chip.org
 * 
 *       All works licensed by the Lesser GPL
 * @link http://www.gnu.org/licenses/lgpl.html
 * 
 *       Portions of this code donated by Ana Holzbach, David Berkowicz, Ahmad
 *       Namani, Henry Chueh and the MGH Lab of Computer Science
 * @link http://www.lcs.mgh.harvard.edu/
 * 
 */
public class DynamicLoadingException extends Exception {

	private static final long serialVersionUID = -6988749616251765396L;

	/**
     * 
     */
	public DynamicLoadingException() {
	}

	/**
	 * @param message
	 */
	public DynamicLoadingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DynamicLoadingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DynamicLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
