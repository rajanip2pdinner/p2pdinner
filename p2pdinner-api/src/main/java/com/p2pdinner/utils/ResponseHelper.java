package com.p2pdinner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p2pdinner.domain.DinnerCart;
import com.p2pdinner.domain.DinnerCartItem;

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
