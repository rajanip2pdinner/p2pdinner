package com.p2pdinner.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2pdinner.service.DinnerListingDataService;
import com.p2pdinner.service.PlacesDataService;
import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlacesScheduledTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlacesScheduledTask.class);
	
	@Autowired
	private DinnerListingDataService dinnerListingDataService;
	
	@Autowired
	private PlacesDataService placesDataService;
	
	@Scheduled(fixedDelay = 5 * 60 * 1000)
	@Transactional
	public void runTask() {
		Collection<DinnerListing>  currentListings = dinnerListingDataService.currentListings();
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<DinnerListing> tobeSaved = new ArrayList<DinnerListing>();
			Collection<DinnerListing> expiredListings = dinnerListingDataService.expiredMarkedListings();
			for(DinnerListing listing : expiredListings) {
				String response = placesDataService.deletePlace(listing.getPlaceId());
				JsonNode rootNode = mapper.readTree(response);
				JsonNode status = rootNode.get("status");
				if (StringUtils.isNotEmpty(status.asText()) &&
						(status.asText().equalsIgnoreCase("OK")) || status.asText().equalsIgnoreCase("NOT_FOUND") )  {
					listing.setMarked(Boolean.FALSE);
					tobeSaved.add(listing);
				}
			}
			if (!tobeSaved.isEmpty()) {
				dinnerListingDataService.saveListings(tobeSaved);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
}
