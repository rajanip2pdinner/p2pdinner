package com.p2p.poc;

import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class GeocodingApiParserTest {
	@Test
	public void testParser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = getClass().getResourceAsStream("/geocode.json");
		JsonNode rootNode = mapper.readTree(inputStream);
		JsonNode otherNode = rootNode.get("results");
		for(final JsonNode node : otherNode) {
			JsonNode geometryNode = node.path("geometry").path("location");
			System.out.println(geometryNode);
		}
	}	
}
