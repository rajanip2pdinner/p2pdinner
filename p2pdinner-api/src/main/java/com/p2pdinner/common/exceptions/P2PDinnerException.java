package com.p2pdinner.common.exceptions;

import org.apache.http.HttpStatus;

public class P2PDinnerException extends Exception {
	private static final long serialVersionUID = 1L;
	private String errorCode = "ZERO_RESULTS";
	private int httpStatus = HttpStatus.SC_OK;
	
	public P2PDinnerException() {
	}
	
	public P2PDinnerException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}
	
	public P2PDinnerException(int httpStatus, String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public int getHttpStatus() {
		return httpStatus;
	}
	
}
