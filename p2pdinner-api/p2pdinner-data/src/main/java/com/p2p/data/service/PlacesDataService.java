package com.p2p.data.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.p2p.data.repositories.DinnerListingRepository;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.MenuItem;
import com.p2p.domain.PlaceSearchResponse;
import com.p2p.domain.vo.Address;
import com.p2p.domain.vo.EntityBuilder;
import com.p2p.rest.client.PlacesSearchRequest;
import com.p2p.rest.client.PlacesSearchRequestFactory;
import com.p2p.utils.PlacesSearchType;

@Component
public class PlacesDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlacesDataService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PlacesSearchRequestFactory placesSearchFactory;
	
	@Autowired
	private DinnerListingRepository dinnerListingRepository;
	
	@Autowired
	private EntityBuilder<Address, String> addressEntityBuilder;
	
	public String addPlace(DinnerListing dinnerListing) throws Exception {
		String addPlaceApiUrl = env.getProperty("google.places.api.add.url");
		String placeApiKey = env.getProperty("google.api.key");
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = mapper.getNodeFactory();
		ObjectNode rootNode = factory.objectNode();
		ObjectNode locationNode = rootNode.objectNode();
		MenuItem u = dinnerListing.getMenuItem();
		String addr = StringUtils.join(new String[] {dinnerListing.getAddressLine1(), dinnerListing.getAddressLine2(), dinnerListing.getCity(), dinnerListing.getState(), dinnerListing.getZipCode()}, " ");
		String geoResponse = this.getGeoCode(addr);
		Address address = addressEntityBuilder.build(geoResponse);
		if (address != null) {
			locationNode.put("lat", address.getLatitude());
			locationNode.put("lng", address.getLongitude());
		} else {
			locationNode.put("lat", u.getUserProfile().getLatitude());
			locationNode.put("lng", u.getUserProfile().getLongitude());
		}
		rootNode.put("location", locationNode);
		rootNode.put("accuracy", 50);
		rootNode.put("name", String.format("p2pdinner " + RandomStringUtils.randomAlphabetic(8)));
		ArrayNode arrayNode = rootNode.putArray("types");
		arrayNode.add("other");
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("key", placeApiKey);
		LOGGER.info(rootNode.toString());
		ResponseEntity<String> response = restTemplate.postForEntity(addPlaceApiUrl, rootNode.toString(), String.class, vars);
		return response.getBody();
	}
	
	public String deletePlace(String placeId) throws Exception {
		String deletePlaceUrl = env.getProperty("google.places.api.delete.url");
		String placeApiKey = env.getProperty("google.api.key");
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = mapper.getNodeFactory();
		ObjectNode rootNode = factory.objectNode();
		rootNode.put("place_id", placeId);
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("key", placeApiKey);
		ResponseEntity<String> response = restTemplate.postForEntity(deletePlaceUrl, rootNode.toString(), String.class, vars);
		return response.getBody();
	}
	
	public Collection<DinnerListing> search(Double latitude, Double longitude, PlacesSearchType placesSearchType) throws Exception {
		ResponseEntity<String> response = null;
		PlacesSearchRequest placesSearchReq = null;
		switch (placesSearchType) {
		case RADAR_SEARCH:
			placesSearchReq = this.placesSearchFactory.getPlacesSearchRequest("radarSearchRequest");
			response = placesSearchReq.makeRequest(latitude, longitude);
			break;
		case NEARBY_SEARCH:
			placesSearchReq = this.placesSearchFactory.getPlacesSearchRequest("nearbySearchRequest");
			response = placesSearchReq.makeRequest(latitude, longitude);
			break;
		case TEXT_SEARCH:
			break;
		default:
		}
		Collection<PlaceSearchResponse> responses = parseResponse(response);
		if ( responses != null && !responses.isEmpty()) {
			Set<String> placeIds = new HashSet<String>();
			for(PlaceSearchResponse r :  responses) {
				placeIds.add(r.getPlaceId());
			}
			Collection<DinnerListing> listings = dinnerListingRepository.findByPlaceIdIn(placeIds);
			return listings;
		}
		return null;
	}
	
	
	private Collection<PlaceSearchResponse> parseResponse(ResponseEntity<String> response) throws Exception {
		Collection<PlaceSearchResponse> searchResult = new ArrayList<PlaceSearchResponse>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(response.getBody());
		JsonNode statusNode = rootNode.get("status");
		if ( statusNode == null || StringUtils.isEmpty(statusNode.getTextValue())) {
			return null;
		}
		if (statusNode.getTextValue().equalsIgnoreCase("OK")) {
			JsonNode resultsNode = rootNode.get("results");
			for(JsonNode result : resultsNode) {
				PlaceSearchResponse r = new PlaceSearchResponse();
				r.setId(result.get("id").getTextValue());
				r.setLatitude(result.path("geometry").path("location").path("lat").getDoubleValue());
				r.setLongitude(result.path("geometry").path("location").path("lng").getDoubleValue());
				r.setPlaceId(result.path("place_id").getTextValue());
				r.setReference(result.path("reference").getTextValue());
				searchResult.add(r);
			}
		}
		return searchResult;
	}
	
	public String getGeoCode(String address) {
		Map<String, String> urlVariables = new HashMap<String,String>();
		urlVariables.put("key", env.getProperty("google.api.key"));
		urlVariables.put("address", address);
		String response = restTemplate.getForObject(env.getProperty("google.geocoding.api.url"), String.class, urlVariables);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(response);
		}
		return response;
	}

	public Optional<DistanceMatrixRow> getDistance(LatLng source, LatLng destination, Locale locale) {
		GeoApiContext apiContext = new GeoApiContext();
		apiContext.setApiKey(env.getProperty("google.api.key"));
		apiContext.setConnectTimeout(120, TimeUnit.MINUTES);
		DistanceMatrixApiRequest distanceMatrixApiRequest = new DistanceMatrixApiRequest(apiContext);
		distanceMatrixApiRequest.origins(source);
		distanceMatrixApiRequest.destinations(destination);
		if (locale.equals(Locale.US)) {
			distanceMatrixApiRequest.units(Unit.IMPERIAL);
		} else {
			distanceMatrixApiRequest.units(Unit.METRIC);
		}
		
		Optional<DistanceMatrix> distanceMatrix = Optional.empty();
		Optional<DistanceMatrixRow> distanceMatrixRow = Optional.empty();
		try {
			distanceMatrix = Optional.of(distanceMatrixApiRequest.await());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (distanceMatrix.isPresent()) {
			distanceMatrixRow = Optional.of(distanceMatrix.get().rows[0]);
		}
		return distanceMatrixRow;
	}
}
