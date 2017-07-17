package com.caboodle.exception;

import com.caboodle.enums.Result;


/**
 * @author harishchauhan
 *
 */
public class ValidationException extends AbstractException {
	private static final long serialVersionUID = 1L;
	
	public ValidationException(String message) {
	    super(Result.FAIL, message, null);
	}

	public ValidationException(Result errorResponse) {
	    super(errorResponse, null, null);
	}
	
	public ValidationException(Result errorResponse, String customErrorMessage) {
	    super(errorResponse, customErrorMessage, null);
	}
	
	public ValidationException(Result errorResponse, String customErrorMessage, Object data) {
	    super(errorResponse, customErrorMessage, data);
	}
}
