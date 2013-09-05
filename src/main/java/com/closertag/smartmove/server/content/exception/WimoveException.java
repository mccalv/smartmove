/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 18 Dec 2009
 * mccalv
 * WimoveException
 * 
 */
package com.closertag.smartmove.server.content.exception;

/**
 * A generic Exception class
 * 
 * @author mccalv
 * 
 */
public class WimoveException extends RuntimeException {

	public static enum ExceptionType {

		INVALID_API_KEY(101), MISSING_PARAMETER(100), INVALID_DATE_FORMAT(102), GENERIC_EXCEPTION(
				500), AREA_INPUT_INVALID(103);

		private int code;

		ExceptionType(int code) {

			this.code = code;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @param code
		 *            the code to set
		 */
		public void setCode(int code) {
			this.code = code;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3236902932080127142L;

	private Throwable exception;
	private ExceptionType exceptionType;
	private String message;

	/**
	 * 
	 * @param exception
	 * @param exceptionType
	 */
	public WimoveException(ExceptionType exceptionType, Throwable exception) {
		this.exception = exception;
		this.exceptionType = exceptionType;
	}

	public WimoveException(ExceptionType exceptionType, String message) {
		this.message = message;
		this.exceptionType = exceptionType;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * @return the exceptionType
	 */
	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType
	 *            the exceptionType to set
	 */
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
