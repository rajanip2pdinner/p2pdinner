package com.p2p.messaging.exceptions;

import com.p2p.data.exceptions.Reason;

/**
 * Messaging Exception
 * @author rajani@p2pdinner.com
 * @since 1.0
 */
public class P2PMessagingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Reason reason;
	
	public P2PMessagingException() {
		super();
	}
	
	public P2PMessagingException(String message) {
		super(message);
	}
	
	public P2PMessagingException(Reason reason, String message) {
		super(message);
		this.reason = reason;
	}
	
	public Reason getReason(){
		return this.reason;
	}
}
