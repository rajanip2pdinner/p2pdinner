package com.p2pdinner.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
		vars.put("location", StringUtils.arrayToDelimitedString(new Double[] { latitude, longitude }, ","));
		LOGGER.info("Nearby Search Uri => {}", radarSearchUri);
		return restTemplate.getForEntity(radarSearchUri, String.class, vars);
	}

	@Override
	public ResponseEntity<String> makeNextPageRequest(Double latitude, Double longitude, String pageToken)
			throws Exception {
		LOGGER.info("Performing radar search with lat => {}, lng => {}, pageToken => {}", latitude, longitude, pageToken);
		LOGGER.info("Performing nearby search with following values");
		RestTemplate restTemplate = new RestTemplate();
		String apiKey = env.getProperty(API_KEY);
		String radarSearchUri = env.getProperty(NEARBY_PAGINATION_SEARCH_URI_KEY);
		
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("key", apiKey);
		vars.put("location", StringUtils.arrayToDelimitedString(new Double[] { latitude, longitude }, ","));
		vars.put("pagetoken", pageToken);
		LOGGER.info("Nearby Search Uri => {}", radarSearchUri);
		URI uri = UriComponentsBuilder.fromUriString(radarSearchUri)
				.queryParam("key", apiKey)
				.queryParam("location", StringUtils.arrayToDelimitedString(new Double[] { latitude, longitude }, ","))
				.queryParam("pagetoken", pageToken)
				.build()
				.toUri();
		LOGGER.info("Nearby Search Uri after expanding variables => {}", uri.toString());
		return restTemplate.getForEntity(uri, String.class);
	}

}
