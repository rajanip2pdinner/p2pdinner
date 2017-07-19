package com.p2pdinner.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RadarSearchRequest implements PlacesSearchRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RadarSearchRequest.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<String> makeRequest(Double latitude, Double longitude) throws Exception {
		LOGGER.info("Performing radar search with lat => {}, lng => {}", latitude, longitude);
		String apiKey = env.getProperty(API_KEY);
		String radarSearchUri = env.getProperty(RADAR_SEARCH_URI_KEY);
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("key", apiKey);
		vars.put("location", StringUtils.arrayToCommaDelimitedString(new Double[]{latitude, longitude}));
		LOGGER.info("Radar Search Uri => {}" , radarSearchUri);
		return restTemplate.getForEntity(radarSearchUri, String.class, vars);
	}

	@Override
	public ResponseEntity<String> makeNextPageRequest(Double latitude, Double longitude, String pageToken)
			throws Exception {
		LOGGER.info("Performing radar search with lat => {}, lng => {}", latitude, longitude);
		String apiKey = env.getProperty(API_KEY);
		String radarSearchUri = env.getProperty(RADAR_PAGINATION_SEARCH_URI_KEY);
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("key", apiKey);
		vars.put("location", StringUtils.arrayToCommaDelimitedString(new Double[] { latitude, longitude}));
		vars.put("pagetoken", pageToken);
		LOGGER.info("Radar Search Uri => {}" , radarSearchUri);
		return restTemplate.getForEntity(radarSearchUri, String.class, vars);
	}

}
