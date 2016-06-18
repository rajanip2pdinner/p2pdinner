package com.p2p.common.exceptions;


public class P2PDinnerException extends Exception {
	private static final long serialVersionUID = 1L;
	private String errorCode = "ZERO_RESULTS";
	
	public P2PDinnerException() {
	}
	
	public P2PDinnerException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
}
