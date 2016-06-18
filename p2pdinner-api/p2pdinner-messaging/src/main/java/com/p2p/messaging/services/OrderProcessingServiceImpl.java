package com.p2p.messaging.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.p2p.data.service.DinnerCartDataService;
import com.p2p.messaging.config.P2PMessagingServerContext;

@Component
public class OrderProcessingServiceImpl implements OrderProcessingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingServiceImpl.class);
	
	@Autowired
	private DinnerCartDataService dinnerCartDataService;
	
	@Autowired
	private RabbitTemplate amqpTemplate;
	
	public void placeOrder(Integer profileId, Integer dinnerCartId) throws Exception {
		LOGGER.info("Placing order for processing {}", dinnerCartId);
		amqpTemplate.convertAndSend(P2PMessagingServerContext.P2PDINNER_REQUEST_ROUTING_KEY, dinnerCartId);
	}

}
