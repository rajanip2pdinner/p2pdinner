package com.p2pdinner.client;

import org.springframework.http.ResponseEntity;

public interface PlacesSearchRequest {
	
	static final String API_KEY = "google.api.key";
	static final String RADAR_SEARCH_URI_KEY = "google.places.api.radarsearch.url";
	static final String NEARBY_SEARCH_URI_KEY = "google.places.api.nearbysearch.url";
	static final String LATLNG_SEARCH_URI_KEY = "google.geocoding.coordinate.search.api.url"; 
	
	static final String LATLNG_PAGINATION_SEARCH_URI_KEY = "google.geocoding.coordinate.pagination.search.api.url";
	static final String RADAR_PAGINATION_SEARCH_URI_KEY = "google.places.api.pagination.radarsearch.url";
	static final String NEARBY_PAGINATION_SEARCH_URI_KEY = "google.places.api.pagination.nearbysearch.url";
	
	ResponseEntity<String> makeRequest(Double latitude, Double longitude) throws Exception;
	ResponseEntity<String> makeNextPageRequest(Double latitude, Double longitude, String pageToken) throws Exception;
}
