package com.p2p.scheduled.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.p2p.data.service.DinnerListingDataService;
import com.p2p.data.service.PlacesDataService;
import com.p2p.domain.DinnerListing;

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
			for(DinnerListing listing : currentListings) {
				if (listing.getMarked() == null || !listing.getMarked() ) {
					String response = placesDataService.addPlace(listing);
					JsonNode rootNode = mapper.readTree(response);
					JsonNode status = rootNode.get("status");
					if (StringUtils.isNotEmpty(status.getTextValue()) && status.getTextValue().equalsIgnoreCase("OK")) {
						JsonNode placeId = rootNode.get("place_id");
						listing.setPlaceId(placeId.getTextValue());
						listing.setMarked(Boolean.TRUE);
						tobeSaved.add(listing);
					}
				}
			}
			Collection<DinnerListing> expiredListings = dinnerListingDataService.expiredMarkedListings();
			for(DinnerListing listing : expiredListings) {
				String response = placesDataService.deletePlace(listing.getPlaceId());
				JsonNode rootNode = mapper.readTree(response);
				JsonNode status = rootNode.get("status");
				if (StringUtils.isNotEmpty(status.getTextValue()) && 
						(status.getTextValue().equalsIgnoreCase("OK")) || status.getTextValue().equalsIgnoreCase("NOT_FOUND") )  {
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
