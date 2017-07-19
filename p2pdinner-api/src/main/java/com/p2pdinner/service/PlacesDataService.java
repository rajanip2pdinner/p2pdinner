package com.p2pdinner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.*;
import com.p2pdinner.repositories.DinnerListingRepository;
import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.PlaceSearchResponse;
import com.p2pdinner.domain.vo.Address;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.client.PlacesSearchRequest;
import com.p2pdinner.client.PlacesSearchRequestFactory;
import com.p2pdinner.utils.PlacesSearchType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		String addr = StringUtils.join(new String[] { dinnerListing.getAddressLine1(), dinnerListing.getAddressLine2(),
				dinnerListing.getCity(), dinnerListing.getState(), dinnerListing.getZipCode() }, " ");
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
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("key", placeApiKey);
		LOGGER.info(rootNode.toString());
		ResponseEntity<String> response = restTemplate.postForEntity(addPlaceApiUrl, rootNode.toString(), String.class,
				vars);
		return response.getBody();
	}

	public String deletePlace(String placeId) throws Exception {
		String deletePlaceUrl = env.getProperty("google.places.api.delete.url");
		String placeApiKey = env.getProperty("google.api.key");
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = mapper.getNodeFactory();
		ObjectNode rootNode = factory.objectNode();
		rootNode.put("place_id", placeId);
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("key", placeApiKey);
		ResponseEntity<String> response = restTemplate.postForEntity(deletePlaceUrl, rootNode.toString(), String.class,
				vars);
		return response.getBody();
	}

	public Collection<DinnerListing> search(Double lat, Double lng, PlacesSearchType placesSearchType)
			throws Exception {
		List<PlacesSearchResult> placeSearchResults = Collections.synchronizedList(new ArrayList<PlacesSearchResult>());
		String placeApiKey = env.getProperty("google.api.key");
		GeoApiContext geoApiContext = new GeoApiContext().setApiKey(placeApiKey);
		LatLng latlng = new LatLng(lat, lng);
		String nextPageToken = StringUtils.EMPTY;
		switch (placesSearchType) {
		case RADAR_SEARCH:
			PlacesSearchResponse radarSearchResponse = PlacesApi.radarSearchQuery(geoApiContext, latlng, 25000)
					.keyword("p2pdinner").awaitIgnoreError();
			if (StringUtils.isNotEmpty(radarSearchResponse.nextPageToken)) {
				nextPageToken = radarSearchResponse.nextPageToken;
			}
			placeSearchResults.addAll(Arrays.asList(radarSearchResponse.results));

			break;
		case NEARBY_SEARCH:
			do {
				PlacesSearchResponse placesSearchResponse = null;
				if (StringUtils.isEmpty(nextPageToken)) {
					placesSearchResponse = PlacesApi.nearbySearchQuery(geoApiContext, latlng)
							.keyword("p2pdinner").radius(50000).await();
				} else {
					LOGGER.info("Waiting for 5 secs");
					Thread.sleep(5000);
					placesSearchResponse =  PlacesApi.nearbySearchNextPage(geoApiContext, nextPageToken).await();	
					//placesSearchResponse = PlacesApi.nearbySearchQuery(geoApiContext, latlng).pageToken(nextPageToken).await();
				}
				if (placesSearchResponse != null) {
					if (StringUtils.isNotEmpty(placesSearchResponse.nextPageToken)) {
						nextPageToken = placesSearchResponse.nextPageToken;
					} else {
						nextPageToken = StringUtils.EMPTY;
					}
					placeSearchResults.addAll(Arrays.asList(placesSearchResponse.results));
				} else {
					nextPageToken = StringUtils.EMPTY;
				}
				LOGGER.info("Place search results size: {}", placeSearchResults.size());
			} while (StringUtils.isNotEmpty(nextPageToken));
			break;
		default:
		}

		Set<String> placeIds = new HashSet<>();
		placeIds = placeSearchResults.stream().map(p -> p.placeId).collect(Collectors.toSet());
		placeIds.stream().forEach(placeId -> LOGGER.info(placeId));
		if (placeIds != null && !placeIds.isEmpty()) {
			Collection<DinnerListing> listings = dinnerListingRepository.findByPlaceIdIn(placeIds);
			return listings;
		} else {
			return null;
		}
	}

	public Collection<DinnerListing> search1(Double latitude, Double longitude, PlacesSearchType placesSearchType)
			throws Exception {
		List<ResponseEntity<String>> responses = Collections.synchronizedList(new ArrayList<>());
		PlacesSearchRequest placesSearchReq = null;
		ObjectMapper mapper = new ObjectMapper();
		String nextPageToken = StringUtils.EMPTY;
		switch (placesSearchType) {
		case RADAR_SEARCH:
			placesSearchReq = this.placesSearchFactory.getPlacesSearchRequest("radarSearchRequest");
			do {
				ResponseEntity<String> response = null;
				if (StringUtils.isEmpty(nextPageToken)) {
					response = placesSearchReq.makeRequest(latitude, longitude);
				} else {
					response = placesSearchReq.makeNextPageRequest(latitude, longitude, nextPageToken);
				}
				JsonNode rootNode = mapper.readTree(response.getBody());
				JsonNode statusNode = rootNode.get("status");
				if (statusNode == null || StringUtils.isEmpty(statusNode.asText())) {
					nextPageToken = StringUtils.EMPTY;
				}
				if (statusNode.asText().equalsIgnoreCase("OK")) {
					responses.add(response);
				}
				JsonNode nextPageTokenNode = rootNode.get("next_page_token");
				if (nextPageTokenNode != null) {
					nextPageToken = nextPageTokenNode.asText();
				} else {
					nextPageToken = StringUtils.EMPTY;
				}
			} while (StringUtils.isNotEmpty(nextPageToken));
			break;
		case NEARBY_SEARCH:
			placesSearchReq = this.placesSearchFactory.getPlacesSearchRequest("nearbySearchRequest");
			do {
				ResponseEntity<String> response = null;
				if (StringUtils.isEmpty(nextPageToken)) {
					response = placesSearchReq.makeRequest(latitude, longitude);
				} else {
					response = placesSearchReq.makeNextPageRequest(latitude, longitude, nextPageToken);
				}
				JsonNode rootNode = mapper.readTree(response.getBody());
				LOGGER.info(rootNode.toString());
				JsonNode statusNode = rootNode.get("status");
				if (statusNode == null || StringUtils.isEmpty(statusNode.asText())) {
					nextPageToken = StringUtils.EMPTY;
				}
				if (statusNode.asText().equalsIgnoreCase("OK")) {
					responses.add(response);
				}
				JsonNode nextPageTokenNode = rootNode.get("next_page_token");
				if (nextPageTokenNode != null) {
					nextPageToken = nextPageTokenNode.asText();
				} else {
					nextPageToken = StringUtils.EMPTY;
				}
			} while (StringUtils.isNotEmpty(nextPageToken));
			break;
		case TEXT_SEARCH:
			break;
		default:
		}
		Collection<PlaceSearchResponse> searchResponses = parseResponse(responses);
		if (searchResponses != null && !searchResponses.isEmpty()) {
			Set<String> placeIds = new HashSet<String>();
			for (PlaceSearchResponse r : searchResponses) {
				placeIds.add(r.getPlaceId());
			}
			Collection<DinnerListing> listings = dinnerListingRepository.findByPlaceIdIn(placeIds);
			return listings;
		}
		return null;
	}

	private Collection<PlaceSearchResponse> parseResponse(Collection<ResponseEntity<String>> responses)
			throws Exception {
		Collection<PlaceSearchResponse> searchResult = new ArrayList<PlaceSearchResponse>();
		for (ResponseEntity<String> response : responses) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(response.getBody());
			JsonNode statusNode = rootNode.get("status");
			if (statusNode == null || StringUtils.isEmpty(statusNode.asText())) {
				return null;
			}
			if (statusNode.asText().equalsIgnoreCase("OK")) {
				JsonNode resultsNode = rootNode.get("results");
				for (JsonNode result : resultsNode) {
					PlaceSearchResponse r = new PlaceSearchResponse();
					r.setId(result.get("id").asText());
					r.setLatitude(result.path("geometry").path("location").path("lat").asDouble());
					r.setLongitude(result.path("geometry").path("location").path("lng").asDouble());
					r.setPlaceId(result.path("place_id").asText());
					r.setReference(result.path("reference").asText());
					searchResult.add(r);
				}
			}
		}
		return searchResult;
	}

	public String getGeoCode(String address) {
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("key", env.getProperty("google.api.key"));
		urlVariables.put("address", address);
		String response = restTemplate.getForObject(env.getProperty("google.geocoding.api.url"), String.class,
				urlVariables);
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
