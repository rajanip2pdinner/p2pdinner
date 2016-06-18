package com.p2p.data.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.p2p.data.config.P2PTestContextConfiguration;
import com.stripe.model.Transfer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class StripeDataServiceTest {
	
	@Autowired
	private StripeDataService stripeDataService;
	
	@Test
	@Ignore
	public void testCharge() throws Exception {
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", 400);
		chargeParams.put("currency", "usd");
		chargeParams.put("card", "tok_15RZ0VJUz5DMZqXz1VqLoid9"); // obtained with Stripe.js
		chargeParams.put("description", "Charge for test@example.com");
		stripeDataService.charge(chargeParams);
	}
	
	@Test
	@Ignore
	public void testPayment() throws Exception {
		Map<String,Object> transferParams = new HashMap<String,Object>();
		transferParams.put("amount", "400");
		transferParams.put("currency", "usd");
		transferParams.put("destination", "acct_1615EmGZGsTl8zsk");
		transferParams.put("application_fee", 100);
		transferParams.put("description", "Transfer for test@example.com");
		Transfer.create(transferParams);
	}
}
