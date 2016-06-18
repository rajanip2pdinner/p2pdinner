package com.p2p.rest.client;

import org.springframework.http.ResponseEntity;

public interface PlacesSearchRequest {
	
	static final String API_KEY = "google.api.key";
	static final String RADAR_SEARCH_URI_KEY = "google.places.api.radarsearch.url";
	static final String NEARBY_SEARCH_URI_KEY = "google.places.api.nearbysearch.url";
	
	ResponseEntity<String> makeRequest(Double latitude, Double longitude) throws Exception;
}
