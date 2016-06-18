package com.p2p.messaging.services;


public interface OrderProcessingService {
	void placeOrder(Integer profileId, Integer cartId) throws Exception;
}
