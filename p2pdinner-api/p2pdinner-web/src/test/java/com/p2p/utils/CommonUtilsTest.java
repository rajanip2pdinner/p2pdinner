package com.p2p.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



public class CommonUtilsTest {
	@Test
	@Ignore
	public void testHashPassword() throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(100);
		for(int i = 0; i < 50; i++) {
			pool.submit(new Runnable() {
				public void run() {
					System.out.println(Thread.currentThread().getName());
					String password = "p2pdinner";
					try {
						String beforeHash = CommonUtils.getHashValue(password);
						String afterHash = CommonUtils.getHashValue(password);
						Assert.assertEquals(beforeHash, afterHash);
					} catch (Exception e) {
						e.getMessage();
					}
					
				}

			});
		}
	}
	
	@Test
	public void responseParser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Resource resource = new ClassPathResource("/geocode.json");
		JsonNode rootNode = mapper.readTree(resource.getInputStream());
		JsonNode results = rootNode.path("results"); 
		if ( results.isArray() ) {
			ArrayNode arrNode = (ArrayNode) results;
			JsonNode addressComponents = arrNode.get(0);
			JsonNode a = addressComponents.get("address_components");
			if (a.isArray()) {
				for(JsonNode n : a) {
					if (n.get("types").get(0).asText().equals("street_number")) {
						System.out.println("[ Street Number ] => " + n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("route")) {
						System.out.println("[ Street ] => " + n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("locality")) {
						System.out.println("[ City ] => " + n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("administrative_area_level_2")) {
						System.out.println("[ County ] => " + n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("administrative_area_level_1")) {
						System.out.println("[ State ] => " + n.get("long_name").asText());
					}  else if (n.get("types").get(0).asText().equals("country")) {
						System.out.println("[ State ] => " + n.get("long_name").asText());
					} else if (n.get("types").get(0).asText().equals("postal_code")) {
						System.out.println("[ Postal Code ] => " + n.get("long_name").asText());
					}  else if (n.get("types").get(0).asText().equals("postal_code_suffix")) {
						System.out.println("[ Postal Code Prefix ] => " + n.get("long_name").asText());
					} 			
				}
			}
		}
		
	}
}
