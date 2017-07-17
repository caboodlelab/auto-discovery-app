package com.caboodle.exception;

import com.caboodle.enums.Result;
/**
 * @author harishchauhan
 *
 */
public class ApplicationException extends AbstractException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ApplicationException(){
	}
	
	public ApplicationException(String message) {
	    super(message);
	}
	
	public ApplicationException(Result errorResponse) {
	    super(errorResponse, null, null);
	}
	
	public ApplicationException(Result errorResponse, String customErrorMessage) {
	    super(errorResponse, customErrorMessage, null);
	}	
	
	public ApplicationException(Result errorResponse, String customErrorMessage, Object data) {
	    super(errorResponse, customErrorMessage, data);
	}	
}
