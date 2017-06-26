package com.p2pdinner.exceptions;

public class P2PDinnerDataException extends Exception {
	private static final long serialVersionUID = 1L;
	private Reason reason;
	
	public P2PDinnerDataException(){
		super();
	}
	
	public P2PDinnerDataException(Reason reason, String errorMessage) {
		super(errorMessage);
		this.reason = reason;
	}
	
	public Reason getReason() {
		return this.reason;
	}
}
