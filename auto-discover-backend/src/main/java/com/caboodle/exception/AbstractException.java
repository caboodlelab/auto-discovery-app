package com.caboodle.exception;

import com.caboodle.enums.Result;

/**
 * @author harishchauhan
 *
 */
public abstract class AbstractException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private Result errorResponse;
	private String customErrorMessage;
	private Object data;
	
	public AbstractException() {
	    this(Result.FAIL.getResultMessage());
	    errorResponse = Result.FAIL;
	}
	
	public AbstractException(String message) {
	    super(message);
	    errorResponse = Result.FAIL;
	}

	public AbstractException(Result errorResponse, String customErrorMessage, Object data) {
	    super((customErrorMessage != null) ? customErrorMessage : errorResponse.getResultMessage());
	    
	    this.errorResponse = errorResponse;
	    this.customErrorMessage = customErrorMessage;
	    this.data = data;
	}
	
	public Result getErrorResponse() {
	    return errorResponse;
	}

	public String getErrorMessage() {
	    if(customErrorMessage != null)
		return customErrorMessage;
	    
	    return errorResponse.getResultMessage();
	}

	public int getErrorCode() {
	    return errorResponse.getResultCode();
	}

	public Object getData() {
	    return data;
	}
}