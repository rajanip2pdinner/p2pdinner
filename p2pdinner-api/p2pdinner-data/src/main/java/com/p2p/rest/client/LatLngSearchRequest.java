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

import com.p2p.domain.vo.Address;
import com.p2p.domain.vo.EntityBuilder;

public class LatLngSearchRequest implements PlacesSearchRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(LatLngSearchRequest.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<String> makeRequest(Double latitude, Double longitude) throws Exception {
		LOGGER.info("Performing radar search with lat => {}, lng => {}", latitude, longitude);
		String apiKey = env.getProperty(API_KEY);
		String latlngSearchRequest = env.getProperty(LATLNG_SEARCH_URI_KEY);
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("key", apiKey);
		vars.put("latlng", StringUtils.join(new Double[] { latitude, longitude}, ","));
		LOGGER.info("Latitude & Longitude Search Uri => {}" , latlngSearchRequest);
		return restTemplate.getForEntity(latlngSearchRequest, String.class, vars);
	}

}
