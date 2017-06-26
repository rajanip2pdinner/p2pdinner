package com.p2pdinner.domain.vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2pdinner.domain.*;
import com.p2pdinner.exceptions.P2PDinnerDataException;
import com.p2pdinner.exceptions.Reason;
import com.p2pdinner.repositories.DinnerCartRepository;
import com.p2pdinner.repositories.DinnerListingRepository;
import com.p2pdinner.repositories.UserProfileRepository;
import com.p2pdinner.service.DinnerCartDataService;
import com.p2pdinner.service.DinnerListingDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class DinnerCartEntityBuilder implements EntityBuilder<DinnerCart, String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DinnerCartEntityBuilder.class);

	@Autowired
	private DinnerCartRepository dinnerCartRepository;

	@Autowired
	private DinnerListingRepository dinnerListingRepository;
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private DinnerCartDataService dinnerCartDataService;
	
	@Autowired
	private DinnerListingDataService dinnerListingDataService;
	
	public DinnerCart build(String request) throws P2PDinnerDataException {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Transforming request to entity {}", request);
		}
		try {
			validate(request);
			DinnerCart dinnerCart = null;
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(request);
			if (rootNode.has("cart_id")) {
				dinnerCart = dinnerCartRepository.findOne(rootNode.path("cart_id").asInt());
			} else {
				dinnerCart = new DinnerCart();
				dinnerCart.setCreatedDate(calendar);
				Integer profileId = rootNode.path("profile_id").asInt();
				UserProfile buyer = userProfileRepository.findOne(profileId);
				dinnerCart.setBuyer(buyer);
			}
			DinnerCartItem cartItem = new DinnerCartItem();
			cartItem.setDinnerCart(dinnerCart);
			DinnerListing dinnerListing = null;
			if (rootNode.has("listing_id")) {
				dinnerListing = dinnerListingRepository.findOne(rootNode.path("listing_id").asInt());
			}
			if (dinnerListing == null) {
				throw new P2PDinnerDataException(Reason.BAD_REQUEST, "Invalid dinner listing id. Is this item listed?");
			}
			cartItem.setDinnerListing(dinnerListing);
			cartItem.setOrderQuantity(rootNode.path("quantity").asInt());
			cartItem.setTotalPrice(dinnerListing.getCostPerItem() * rootNode.path("quantity").asInt());
			if (cartItem.getOrderQuantity() > dinnerListing.getAvailableQuantity()) {
				throw new P2PDinnerDataException(Reason.INSUFFICIENT_QUANTITY, "Ordered quantity exceeds available quantity");
			}
			// check if this item is already present in the cart, if present then update count and price
			Boolean itemAlreadyPresentInCart = false;
			if (dinnerCart.getCartItems() != null && !dinnerCart.getCartItems().isEmpty()) {
				for(DinnerCartItem dci : dinnerCart.getCartItems()) {
					if (!itemAlreadyPresentInCart && dci.getDinnerListing().getId().intValue() == cartItem.getDinnerListing().getId().intValue()) {
						dci.setOrderQuantity(dci.getOrderQuantity() + cartItem.getOrderQuantity());
						dci.setTotalPrice(dci.getDinnerListing().getCostPerItem() * dci.getOrderQuantity());
						itemAlreadyPresentInCart = true;
					}
				}
			}
			if ( !itemAlreadyPresentInCart ) {
				dinnerCart.getCartItems().add(cartItem);
			}
			dinnerCart.setDeliveryType(rootNode.path("delivery_type").asText());
			dinnerCart.setModifiedDate(calendar);
			dinnerCart.setStatus(OrderStatus.ORDER_IN_PROGRESS);
			return dinnerCart;
		} catch (P2PDinnerDataException p2pex) {
			throw p2pex;
		} catch (Exception ex) {
			throw new P2PDinnerDataException(Reason.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	private void validate(String request) throws Exception {
		String[] fields = { "listing_id", "profile_id", "quantity", "delivery_type" };
		if (!StringUtils.hasText(request)) {
			LOGGER.error("no fields not present in request");
			throw new P2PDinnerDataException(Reason.BAD_REQUEST, "Invalid request");
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		for (String field : fields) {
			if (!rootNode.has(field)) {
				LOGGER.error("{} not present in request", field);
				throw new P2PDinnerDataException(Reason.BAD_REQUEST, "Invalid request");
			}
		}
	}

}
