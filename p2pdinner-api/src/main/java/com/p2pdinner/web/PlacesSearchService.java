package com.p2pdinner.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.p2pdinner.service.DinnerCartDataService;
import com.p2pdinner.service.PlacesDataService;
import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.vo.Address;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.web.filters.FilterProcessor;
import com.p2pdinner.client.PlacesSearchRequestFactory;
import com.p2pdinner.utils.PlacesSearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Path("/places")
public class PlacesSearchService {

	private static Logger LOGGER = LoggerFactory.getLogger(PlacesSearchService.class);

	@Autowired
	private PlacesDataService placesDataService;

	@Autowired
	private FilterProcessor<DinnerListing> filterProcessor;

	@Autowired
	private EntityBuilder<Address, String> addressEntityBuilder;

	@Autowired
	private PlacesSearchRequestFactory placesSearchRequestFactory;

	@Autowired
	private DinnerCartDataService dinnerCartDataService;

	@Path("/address")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response address(@QueryParam("lat") Double latitude, @QueryParam("lng") Double longitude) throws Exception {
		if (latitude == null || longitude == null) {
			throw new BadRequestException();
		}
		Address address = addressEntityBuilder.build(this.placesSearchRequestFactory
				.getPlacesSearchRequest("latlngSearchRequest").makeRequest(latitude, longitude).getBody());
		return Response.ok(address).build();
	}

	@Path("/nearbysearch")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response nearbySearch(@Context UriInfo uriInfo) {
		Response response = Response.ok().build();
		Double latitude;
		Double longitude;
		String address;
		String searchQry = "";
		Locale locale = Locale.US;
		try {
			if (uriInfo.getQueryParameters().containsKey("address")) {
				address = uriInfo.getQueryParameters().getFirst("address");
				String geoCodeSearchResult = placesDataService.getGeoCode(address);
				latitude = getLatitude(geoCodeSearchResult);
				longitude = getLongitude(geoCodeSearchResult);
			} else {
				latitude = Double.parseDouble(uriInfo.getQueryParameters().getFirst("latitude"));
				longitude = Double.parseDouble(uriInfo.getQueryParameters().getFirst("longitude"));
			}
			if (uriInfo.getQueryParameters().containsKey("q")) {
				searchQry = uriInfo.getQueryParameters().getFirst("q");
			}
			if (uriInfo.getQueryParameters().containsKey("locale")) {
				locale = StringUtils.parseLocaleString(uriInfo.getQueryParameters().getFirst("locale"));
			}
			LOGGER.info("Running nearby search with latitdue - {} and longitude - {}", latitude, longitude);

			Collection<DinnerListing> results = placesDataService.search(latitude, longitude,
					PlacesSearchType.NEARBY_SEARCH);
			if (results != null && !results.isEmpty() && !StringUtils.isEmpty(searchQry)) {
				results = filterProcessor.applyFilters(searchQry, results);
			}
			Map<Integer, List<DinnerListing>> listings = new HashMap<>();
			if (results != null && !results.isEmpty()) {
				for (DinnerListing lst : results) {
					if (listings.containsKey(lst.getMenuItem().getUserProfile().getId())) {
						List<DinnerListing> lsts = listings.get(lst.getMenuItem().getUserProfile().getId());
						lsts.add(lst);
					} else {
						List<DinnerListing> lsts = new ArrayList<DinnerListing>();
						lsts.add(lst);
						listings.put(lst.getMenuItem().getUserProfile().getId(), lsts);
					}
				}
			}
			String rsp = prepareResponse(listings, latitude, longitude, locale);
			response = Response.ok(rsp).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

	private String prepareResponse(Map<Integer, List<DinnerListing>> results, Double latitude, Double longitude,
			Locale locale) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "OK");
		List<Map<String, Object>> resultsArray = new ArrayList<>();
		LatLng source = new LatLng(latitude, longitude);
		for (Map.Entry<Integer, List<DinnerListing>> e : results.entrySet()) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("profile_id", e.getKey());
			Double rating = dinnerCartDataService.avgSellerRating(e.getKey());
			if (rating != null) {
				resultMap.put("seller_rating", rating);
			}
			List<Map<String, Object>> menuItems = new ArrayList<>();
			for (DinnerListing listing : e.getValue()) {
				Map<String, Object> menuItem = new HashMap<>();
				menuItem.put("title", listing.getMenuItem().getTitle());
				menuItem.put("description", listing.getMenuItem().getDescription());
				menuItem.put("profile_id", listing.getMenuItem().getUserProfile().getId());
				menuItem.put("profile_name", listing.getMenuItem().getUserProfile().getName());
				menuItem.put("name", listing.getMenuItem().getUserProfile().getEmailAddress());
				menuItem.put("image_uri", listing.getMenuItem().getImageUri());
				Map<String, Object> dinnerListing = new HashMap<>();
				dinnerListing.put("listing_id", listing.getId());
				dinnerListing.put("cost_per_item", listing.getCostPerItem());
				dinnerListing.put("quantity_available", listing.getAvailableQuantity());
				dinnerListing.put("ordered_quantity", listing.getOrderQuantity());
				dinnerListing.put("start_time", sdf.format(listing.getStartTime()));
				dinnerListing.put("close_time", sdf.format(listing.getCloseTime()));
				dinnerListing.put("end_date", sdf.format(listing.getEndTime()));
				menuItem.put("listing", dinnerListing);
				List<String> categories = new ArrayList<>();
				if (StringUtils.hasText(listing.getCategories())) {
					categories = Arrays
							.asList(StringUtils.tokenizeToStringArray(listing.getCategories(), ",", true, true));
				} else {
					for (DinnerCategory category : listing.getMenuItem().getDinnerCategories()) {
						categories.add(category.getName());
					}
				}
				List<String> specialNeeds = new ArrayList<>();
				if (StringUtils.hasText(listing.getSpecialNeeds())) {
					specialNeeds = Arrays
							.asList(StringUtils.tokenizeToStringArray(listing.getSpecialNeeds(), ",", true, true));
				} else {
					for (DinnerSpecialNeeds specialNeed : listing.getMenuItem().getDinnerSpecialNeeds()) {
						specialNeeds.add(specialNeed.getName());
					}
				}

				List<String> deliverytypes = new ArrayList<>();
				if (StringUtils.hasText(listing.getDeliveryTypes())) {
					deliverytypes = Arrays
							.asList(StringUtils.tokenizeToStringArray(listing.getDeliveryTypes(), ",", true, true));
				} else {
					for (DinnerDelivery delivery : listing.getMenuItem().getDinnerDeliveries()) {
						deliverytypes.add(delivery.getName());
					}
				}
				menuItem.put("categories", categories);
				menuItem.put("special_needs", specialNeeds);
				menuItem.put("delivery", deliverytypes);
				String destinationAddress = StringUtils.arrayToDelimitedString(
						new String[] { StringUtils.hasText(listing.getAddressLine1()) ? listing.getAddressLine1() : "",
								StringUtils.hasText(listing.getAddressLine2()) ? listing.getAddressLine2() : "",
								StringUtils.hasText(listing.getCity()) ? listing.getCity() : "",
								StringUtils.hasText(listing.getState()) ? listing.getState() : "",
								StringUtils.hasText(listing.getZipCode()) ? listing.getZipCode() : "" },
						" ");
				String geoCodeSearchResult = placesDataService.getGeoCode(destinationAddress);
				if (StringUtils.hasText(geoCodeSearchResult)) {
					Double destinationLat = getLatitude(geoCodeSearchResult);
					Double destinationLng = getLongitude(geoCodeSearchResult);
					LatLng destination = new LatLng(destinationLat, destinationLng);
					Map<String, Object> location = new HashMap<>();
					location.put("lat", destinationLat);
					location.put("lng", destinationLng);
					Optional<DistanceMatrixRow> distanceMatrixRow = placesDataService.getDistance(source, destination,
							locale);

					if (!distanceMatrixRow.isPresent()) {
						location.put("distance", "unknown");
						location.put("distanceInMeters", 999999999L);
						location.put("address", destinationAddress.toString());
					} else {
						location.put("distance", distanceMatrixRow.get().elements[0].distance.humanReadable);
						location.put("distanceInMeters", distanceMatrixRow.get().elements[0].distance.inMeters);
						location.put("address", destinationAddress.toString());
					}
					menuItem.put("location", location);
					menuItems.add(menuItem);
				}
			}
			resultMap.put("menu_items", menuItems);
			resultsArray.add(resultMap);
		}
		responseMap.put("results", resultsArray);

		ObjectMapper mapper = new ObjectMapper();
		/*
		 * JsonNodeFactory factory = mapper.getNodeFactory(); ObjectNode
		 * rootNode = factory.objectNode(); rootNode.put("status", "OK");
		 * ArrayNode resultsNode = rootNode.putArray("results"); for
		 * (Map.Entry<Integer, List<DinnerListing>> e : results.entrySet()) {
		 * Integer profileId = e.getKey(); List<DinnerListing> listings =
		 * e.getValue(); ObjectNode resultNode = resultsNode.addObject(); LatLng
		 * source = new LatLng(latitude, longitude); //LatLng destination = new
		 * LatLng(profile.getLatitude(), profile.getLongitude());
		 * resultNode.put("profile_id", profileId); ArrayNode menuItemsNode =
		 * resultNode.putArray("menu_items"); for (DinnerListing listing :
		 * listings) { menuItemsNode.add(mapper.convertValue(listing,
		 * JsonNode.class)); String destinationAddress = StringUtils.join(new
		 * String[]{listing.getAddressLine1(), listing.getAddressLine2(),
		 * listing.getCity(), listing.getState(), listing.getZipCode()}, " ");
		 * String geoCodeSearchResult =
		 * placesDataService.getGeoCode(destinationAddress); Double
		 * destinationLat = getLatitude(geoCodeSearchResult); Double
		 * destinationLng = getLongitude(geoCodeSearchResult); LatLng
		 * destination = new LatLng(destinationLat, destinationLng); ObjectNode
		 * locationNode = resultNode.putObject("location");
		 * locationNode.put("lat", destinationLat); locationNode.put("lng",
		 * destinationLng); locationNode.put("distance",
		 * placesDataService.getDistance(source, destination));
		 * locationNode.put("address", destinationAddress.toString()); }
		 * //resultsNode.add(resultNode); }
		 */
		return mapper.writeValueAsString(responseMap);
	}

	private Double getLatitude(String response) throws Exception {
		Double lat = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(response);
			if (rootNode != null && rootNode.has("results")) {
				JsonNode results = rootNode.get("results");
				if (results != null && results.isArray()) {
					Iterator<JsonNode> iterator = results.iterator();
					JsonNode childNode = iterator.next();
					JsonNode latitudeNode = childNode.path("geometry").path("location").path("lat");
					lat = latitudeNode.asDouble();
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return lat;
	}

	private Double getLongitude(String response) throws Exception {
		Double lng = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(response);
			if (rootNode != null && rootNode.has("results")) {
				JsonNode results = rootNode.get("results");
				if (results.isArray()) {
					Iterator<JsonNode> iterator = results.iterator();
					JsonNode childNode = iterator.next();
					JsonNode latitudeNode = childNode.path("geometry").path("location").path("lng");
					lng = latitudeNode.asDouble();
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return lng;
	}
}
