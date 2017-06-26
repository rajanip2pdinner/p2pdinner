package com.p2pdinner.domain.vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Iterator;

public class AddressEntityBuilder implements EntityBuilder<Address, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Address.class);

	public Address build(String r) throws Exception {
		Address address = new Address();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(r);
		if (!isValidAddress(rootNode)) {
			LOGGER.info("Address not validated... returning null value");
			return null;
		}
//		if (!isPartialMatch(rootNode)) {
//			LOGGER.info("Unable to validate address. Please try again with correct address");
//			return null;
//		}
		address.setLatitude(getLatitude(rootNode));
		address.setLongitude(getLongitude(rootNode));
		JsonNode results = rootNode.path("results");
		if (results.isArray()) {
			ArrayNode arrNode = (ArrayNode) results;
			JsonNode addressComponents = arrNode.get(0);
			JsonNode a = addressComponents.get("address_components");
			if (a.isArray()) {
				for (JsonNode n : a) {
					if (n.get("types").get(0).asText().equals("street_number")) {
						address.setStreetNumber(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("route")) {
						address.setStreet(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("locality")) {
						address.setCity(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("administrative_area_level_2")) {
						address.setCounty(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("administrative_area_level_1")) {
						address.setState(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("country")) {
						address.setCountry(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("postal_code")) {
						address.setPostalCode(n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("postal_code_suffix")) {
						address.setPostalCodeSuffix(n.get("long_name").asText());
					}
				}
			}
		}

		return address;
	}

	private Boolean isPartialMatch(JsonNode rootNode) throws Exception {
		try {
			JsonNode results = rootNode.get("results");
			if (results.isArray()) {
				Iterator<JsonNode> iterator = results.iterator();
				if (iterator.hasNext()) {
					JsonNode childNode = iterator.next();
					if (childNode.has("partial_match")) {
						return true;
					}
				} 
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return false;
	}

	private Double getLatitude(JsonNode rootNode) throws Exception {
		Double lat = null;
		JsonNode results = rootNode.get("results");
		if (results.isArray()) {
			Iterator<JsonNode> iterator = results.iterator();
			JsonNode childNode = iterator.next();
			JsonNode latitudeNode = childNode.path("geometry").path("location").path("lat");
			lat = latitudeNode.asDouble();
		}
		return lat;
	}

	private Double getLongitude(JsonNode rootNode) throws Exception {
		Double lng = null;
		JsonNode results = rootNode.get("results");
		if (results.isArray()) {
			Iterator<JsonNode> iterator = results.iterator();
			JsonNode childNode = iterator.next();
			JsonNode latitudeNode = childNode.path("geometry").path("location").path("lng");
			lng = latitudeNode.asDouble();
		}
		return lng;
	}
	
	private Boolean isValidAddress(JsonNode rootNode) throws Exception {
		if (rootNode.has("status")) {
			String statusValue = rootNode.get("status").asText();
			if (StringUtils.hasText(statusValue) && !statusValue.equalsIgnoreCase("ZERO_RESULTS")) {
				return true;
			}
		}
		return false;
	}

}
