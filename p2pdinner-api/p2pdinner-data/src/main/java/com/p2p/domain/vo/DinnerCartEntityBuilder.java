package com.p2p.domain.vo;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.data.exceptions.P2PDinnerDataException;
import com.p2p.data.exceptions.Reason;
import com.p2p.data.repositories.DinnerCartRepository;
import com.p2p.data.repositories.DinnerListingRepository;
import com.p2p.data.repositories.UserProfileRepository;
import com.p2p.data.service.DinnerCartDataService;
import com.p2p.data.service.DinnerListingDataService;
import com.p2p.domain.DinnerCart;
import com.p2p.domain.DinnerCartItem;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.OrderStatus;
import com.p2p.domain.UserProfile;

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
		Calendar calendar = Calendar.getInstance();
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
				Integer profileId = rootNode.path("profile_id").getIntValue();
				UserProfile buyer = userProfileRepository.findOne(profileId);
				dinnerCart.setBuyer(buyer);
			}
			DinnerCartItem cartItem = new DinnerCartItem();
			cartItem.setDinnerCart(dinnerCart);
			DinnerListing dinnerListing = null;
			if (rootNode.has("listing_id")) {
				dinnerListing = dinnerListingRepository.findOne(rootNode.path("listing_id").getIntValue());
			}
			if (dinnerListing == null) {
				throw new P2PDinnerDataException(Reason.BAD_REQUEST, "Invalid dinner listing id. Is this item listed?");
			}
			cartItem.setDinnerListing(dinnerListing);
			cartItem.setOrderQuantity(rootNode.path("quantity").getIntValue());
			cartItem.setTotalPrice(dinnerListing.getCostPerItem() * rootNode.path("quantity").getIntValue());
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
			dinnerCart.setDeliveryType(rootNode.path("delivery_type").getTextValue());
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
		if (StringUtils.isEmpty(request)) {
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
