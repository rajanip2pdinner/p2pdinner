package com.p2p.poc;

import java.io.Serializable;

public class HelloWorld implements Serializable {
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
