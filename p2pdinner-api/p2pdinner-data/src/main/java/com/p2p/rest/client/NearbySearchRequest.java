package com.p2p.rest.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NearbySearchRequest implements PlacesSearchRequest {

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(NearbySearchRequest.class);

	public ResponseEntity<String> makeRequest(Double latitude, Double longitude) throws Exception {
		LOGGER.info("Performing radar search with lat => {}, lng => {}", latitude, longitude);
		String apiKey = env.getProperty(API_KEY);
		String radarSearchUri = env.getProperty(NEARBY_SEARCH_URI_KEY);
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("key", apiKey);
		vars.put("location", StringUtils.join(new Double[] { latitude, longitude }, ","));
		LOGGER.info("Nearby Search Uri => {}", radarSearchUri);
		return restTemplate.getForEntity(radarSearchUri, String.class, vars);
	}

}
