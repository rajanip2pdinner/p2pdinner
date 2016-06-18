package com.p2p.messaging.services;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.common.notifications.EmailNotification;
import com.p2p.data.exceptions.Reason;
import com.p2p.data.service.DinnerCartDataService;
import com.p2p.data.service.StripeDataService;
import com.p2p.domain.DinnerCart;
import com.p2p.domain.DinnerCartItem;
import com.p2p.domain.OrderStatus;
import com.p2p.messaging.config.P2PMessagingServerContext;
import com.p2p.messaging.exceptions.P2PMessagingException;

public class OrderProcessingHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingHandler.class);

	@Autowired
	private DinnerCartDataService dinnerCartDataService;

	@Autowired
	private StripeDataService stripeDataService;
	
	@Autowired
	private EmailNotification emailNotification;

	@Autowired
	private RabbitTemplate amqpTemplate;
	
	private static final String ORDER_CONFIRMATION_SUBJECT = "/order-confirmation-subject.html";
	private static final String ORDER_CONFIRMATION_BODY = "/order-confirmation.html";
	
	/**
	 * Process order message for a given cartId
	 * 
	 * @param cartId
	 *            Cart Identifier
	 * @return void
	 * @throws P2PMessagingException
	 * @see P2PMessagingException
	 **/
	@Transactional(rollbackOn = Exception.class)
	public void handleMessage(Integer cartId) throws Exception {
		LOGGER.info("####  Processing order => {}", cartId);
		DinnerCart dinnerCart = null;
		try {
			dinnerCart = dinnerCartDataService.getCart(cartId);
			if (dinnerCart.getStatus() != OrderStatus.READY_TO_PROCESS) {
				throw new P2PMessagingException(Reason.ORDER_NOT_READY, "Order not ready to be processed.");
			}
			String passCode = RandomStringUtils.randomAlphanumeric(20);
			dinnerCart.setPassCode(passCode);
			dinnerCart.setStatus(OrderStatus.RECEIVED);
			dinnerCart.setModifiedDate(Calendar.getInstance());
			chargeCustomer(dinnerCart);
			sendNotification(dinnerCart);
		} catch (P2PMessagingException excep) {
			switch (excep.getReason()) {
			case ORDER_NOT_READY:
				dinnerCart.setStatus(OrderStatus.PROCESSING_ERROR);
				break;
			case NO_PAYMENT_INFORMATION:
				dinnerCart.setStatus(OrderStatus.MISSING_PAYMENT_INFORMATION);
				break;
			default:
				dinnerCart.setStatus(OrderStatus.UNKNOWN_ERROR);
			}
			dinnerCart.setModifiedDate(Calendar.getInstance());
		} catch (Exception e) {
			dinnerCart.setStatus(OrderStatus.PROCESSING_ERROR);
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				dinnerCartDataService.saveOrUpdateCart(dinnerCart);
				amqpTemplate.convertAndSend(P2PMessagingServerContext.P2PDINNER_REQUEST_EXCEPTION_ROUTING_KEY, cartId);
			} catch (Exception e) {
				throw e;
			}
		}

	}

	/**
	 * Charge buyer based from stripeCustomerId. If no stripeCustomerId, set
	 * error to NO_PAYMENT_INFORMATION
	 * 
	 * @param userProfile
	 * @return void
	 * @throws P2PMessagingException
	 * @see P2PMessagingException
	 * 
	 */

	@Transactional
	public void chargeCustomer(DinnerCart dinnerCart) throws P2PMessagingException, Exception {
		try {
			if (StringUtils.isNotEmpty(dinnerCart.getBuyer().getStripeCustomerId())) {
				Double totalPrice =  0d;
				for(DinnerCartItem item : dinnerCart.getCartItems()) {
					totalPrice += item.getTotalPrice();
				}
				String chargeId = stripeDataService.charge(dinnerCart.getBuyer(), dinnerCart.getId(), dinnerCart.getPassCode(), totalPrice);
				dinnerCart.setChargeId(chargeId);
			} else {
				throw new P2PMessagingException(Reason.NO_PAYMENT_INFORMATION, "No payment information for buyer " + dinnerCart.getBuyer().getEmailAddress());
			}
		} catch (P2PMessagingException excep) {
			throw excep;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	private void sendNotification(DinnerCart dinnerCart) {
		Map<String,Object> substVariables = new HashMap<String,Object>();
		substVariables.put("cartItems", dinnerCart.getCartItems());
		substVariables.put("passCode", dinnerCart.getPassCode());
		//emailNotification.sendNotification(new String[] {dinnerCart.getBuyer().getEmailAddress()}, "rajani@p2pdinner.com", ORDER_CONFIRMATION_SUBJECT, ORDER_CONFIRMATION_BODY, substVariables);
	}
}
