package com.p2p.services.aspects;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.data.service.StripeDataService;
// com.p2p.messaging.services.OrderProcessingService;

@Aspect
public class ProcessOrderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessOrderAspect.class);
	
//	@Autowired
//	private OrderProcessingService orderProcessingService;
	
	@Autowired
	private StripeDataService stripeDataService;
	
	//@Pointcut("execution (* com.p2p.services.DinnerCartService.placeOrder(Integer, Integer, String)) && args(profileId, cartId, request)")
	public void placeOrder(Integer profileId, Integer cartId, String request){}
	//@After("placeOrder(profileId,cartId,request)")
	public void placeOrderInQueue(Integer profileId, Integer cartId, String request) throws Exception {
		LOGGER.info("Placing order request {} - {} in queue", profileId, cartId);
		//orderProcessingService.placeOrder(profileId, cartId);
	}
	
	@SuppressWarnings("unchecked")
	//@Before("placeOrder(profileId,cartId,request)")
	public void updateCustomerInformation(Integer profileId, Integer cartId, String request) throws Exception {
		LOGGER.info("Updating customer information...");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		if (rootNode.has("card_details")) {
			JsonNode cardDetailsNode = rootNode.get("card_details");
			Map<String,Object> cardDetails = (Map<String,Object>) mapper.readValue(cardDetailsNode, HashMap.class);
			stripeDataService.saveOrUpdateCustomer(profileId, cardDetails);
		}
	}
	
}
