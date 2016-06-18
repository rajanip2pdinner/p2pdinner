package com.p2p.messaging.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderProcessingExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingExceptionHandler.class);

	public void handleMessage(Integer cartId) {
		LOGGER.info("### Processing exception for cart Id {}", cartId);
	}
}
