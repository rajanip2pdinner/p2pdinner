package com.p2p.rest.helpers;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.p2p.domain.DinnerCart;
import com.p2p.domain.DinnerCartItem;

public class ResponseHelper {
	public static String generateResponse(DinnerCart dinnerCart) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("status", "OK");
		rootNode.put("cartId", dinnerCart.getId());
		ArrayNode items = rootNode.putArray("results");
		Double totalPrice = 0.0;
		for(DinnerCartItem item : dinnerCart.getCartItems()) {
			ObjectNode itemNode = rootNode.objectNode();
			itemNode.put("title", item.getDinnerListing().getMenuItem().getTitle());
			itemNode.put("quantity", item.getOrderQuantity());
			itemNode.put("price", item.getTotalPrice());
			totalPrice += item.getTotalPrice();
			items.add(itemNode);
		}
		rootNode.put("total_price", totalPrice);
		return rootNode.toString();
	}
}
