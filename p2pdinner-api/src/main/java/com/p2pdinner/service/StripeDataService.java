package com.p2pdinner.service;

import com.p2pdinner.repositories.UserProfileRepository;
import com.p2pdinner.domain.UserProfile;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author rajani@p2pdinner.com class to handle stripe api
 */
@Component
//@PropertySource("classpath:/external-services.properties")
public class StripeDataService implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(StripeDataService.class);

	@Autowired
	private Environment env;

	@Autowired
	private UserProfileRepository userProfileRepository;

	public String charge(Map<String, Object> chargeParams) throws Exception {
		try {
			Charge charge = Charge.create(chargeParams);
			return charge.getId();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}

	}

	public String charge(UserProfile profile, Integer cartId, String idempotentKey, Double amount) throws Exception {
		Customer customer = getCustomer(profile.getStripeCustomerId());
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("customer", customer.getId());
		chargeParams.put("currency", "USD");
		Double value = amount * 100;
		chargeParams.put("amount", value.intValue());
		chargeParams.put("description", "P2PDinner payment");
		chargeParams.put("receipt_email", profile.getEmailAddress());
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("cart_id", cartId);
		metadata.put("passcode", idempotentKey);
		chargeParams.put("metadata", metadata);
		return charge(chargeParams);
	}
	
	public String refund(String chargeId) throws Exception {
		Map<String,Object> refundParams = new HashMap<String,Object>();
		Charge charge = Charge.retrieve(chargeId);
		Refund refund = charge.getRefunds().create(refundParams);
		return refund.getId();
	}

	public Customer getCustomer(String customerId) throws Exception {
		return Customer.retrieve(customerId);
	}

	/**
	 * Create a new customer profile or update an existing customer profile in
	 * stripe
	 * 
	 * @param profileId
	 * @param cardDetails
	 * @throws Exception
	 */
	public void saveOrUpdateCustomer(Integer profileId, Map<String, Object> cardDetails) throws Exception {
		UserProfile profile = userProfileRepository.findOne(profileId);
		Customer customer = Customer.retrieve(profile.getStripeCustomerId());
		if (customer == null) {
			createCustomer(cardDetails, profile.getId());
		} else {
			Map<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("profile_id", profile.getId());
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", "Customer for P2PDinner");
			customerParams.put("metadata", metadata);
			customerParams.put("card", cardDetails.get("card"));
			customer.createCard(customerParams);
		}

	}

	public void createCustomer(Map<String, Object> cardDetails, Integer profileId) throws Exception {
		try {
			Map<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("profile_id", profileId);
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", "Customer for P2PDinner");
			customerParams.put("metadata", metadata);
			customerParams.put("card", cardDetails.get("card"));
			// customerParams.putAll(cardDetails);
			Customer customer = Customer.create(customerParams);
			UserProfile userProfile = userProfileRepository.findOne(profileId);
			userProfile.setStripeCustomerId(customer.getId());
			userProfileRepository.save(userProfile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Stripe.apiKey = env.getProperty("stripe.api.key");
	}
}
