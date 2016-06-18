package com.p2p.common.codes;

public enum SuccessCode {
	
	SUCCESS("p2pdinner.success");
	
	String successCode;
	
	SuccessCode(String successCode) {
		this.successCode = successCode;
	}
	
	public String getSuccessCode() {
		return this.successCode;
	}
}
