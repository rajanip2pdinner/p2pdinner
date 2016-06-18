package com.p2p.data.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.data.exceptions.P2PDinnerDataException;
import com.p2p.domain.*;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.p2p.data.repositories.DinnerListingRepository;
import com.p2p.domain.vo.DinnerListingVO;
import com.p2p.domain.vo.EntityBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class DinnerListingDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DinnerListingDataService.class);

    @Autowired
    private DinnerListingRepository dinnerListingRepository;

    @Autowired
    private DinnerCartDataService dinnerCartDataService;

    @Autowired
    private EntityBuilder<DinnerListing, DinnerListingVO> entityBuilder;

    @Autowired
    private ExceptionMessageBuilder exceptionMessageBuilder;

    @Autowired
    private PlacesDataService placesDataService;

    @Transactional
    public DinnerListing updateAvailability(DinnerListingVO listingvo) throws P2PDinnerException {
        try {
            DinnerListing dinnerListing = entityBuilder.build(listingvo);
            DateTime startDateTime = new DateTime(dinnerListing.getStartTime());
            DateTime endDateTime = new DateTime(dinnerListing.getEndTime());
            DateTime closeDateTime = new DateTime(dinnerListing.getCloseTime());
            // startDate and endDate should be after closeDate
            // closeDate should be after current date
            // startDate should be before endDate
            if (startDateTime.isAfter(endDateTime)) {
                throw exceptionMessageBuilder.createException(ErrorCode.INVALID_START_TIME, new String[] {startDateTime.toString(), endDateTime.toString()}, Locale.US);
            }
            if (endDateTime.isBefore(closeDateTime)) {
                throw exceptionMessageBuilder.createException(ErrorCode.INVALID_CLOSE_TIME, new String[] { closeDateTime.toString(), endDateTime.toString()}, Locale.US);
            }
            
			if (StringUtils.hasText(dinnerListing.getMenuItem().getAddressLine1()) || StringUtils.hasText(dinnerListing.getMenuItem().getAddressLine2())) {
				dinnerListing.setAddressLine1(dinnerListing.getMenuItem().getAddressLine1());
				dinnerListing.setAddressLine2(dinnerListing.getMenuItem().getAddressLine2());
				dinnerListing.setCity(dinnerListing.getMenuItem().getCity());
				dinnerListing.setState(dinnerListing.getMenuItem().getState());
				dinnerListing.setZipCode(dinnerListing.getMenuItem().getZipCode());
			} else if (StringUtils.hasText(dinnerListing.getMenuItem().getUserProfile().getAddressLine1()) || StringUtils.hasText(dinnerListing.getMenuItem().getUserProfile().getAddressLine2())) {
				dinnerListing.setAddressLine1(dinnerListing.getMenuItem().getUserProfile().getAddressLine1());
				dinnerListing.setAddressLine2(dinnerListing.getMenuItem().getUserProfile().getAddressLine2());
				dinnerListing.setCity(dinnerListing.getMenuItem().getUserProfile().getCity());
				dinnerListing.setState(dinnerListing.getMenuItem().getUserProfile().getState());
				dinnerListing.setZipCode(dinnerListing.getMenuItem().getUserProfile().getZip());
			}

            if (!StringUtils.hasText(dinnerListing.getAddressLine1()) && !StringUtils.hasText(dinnerListing.getAddressLine2())) {
                throw exceptionMessageBuilder.createException(ErrorCode.INVALID_ADDRESS, new String[] {}, Locale.US);
            }

            if (!CollectionUtils.isEmpty(dinnerListing.getMenuItem().getDinnerCategories())) {
                StringBuffer categories = new StringBuffer();
                for(DinnerCategory dinnerCategory : dinnerListing.getMenuItem().getDinnerCategories()) {
                        categories.append(dinnerCategory.getName()).append(",");
                }
                if (StringUtils.hasText(categories.toString())) {
                    dinnerListing.setCategories(categories.substring(0, categories.length() - 1));
                }
            }
            if (!CollectionUtils.isEmpty(dinnerListing.getMenuItem().getDinnerDeliveries())) {
                StringBuffer deliveryTypes = new StringBuffer();
                for(DinnerDelivery dinnerDelivery : dinnerListing.getMenuItem().getDinnerDeliveries()) {
                    deliveryTypes.append(dinnerDelivery.getName()).append(",");
                }
                if (StringUtils.hasText(deliveryTypes.toString())) {
                    dinnerListing.setDeliveryTypes(deliveryTypes.substring(0, deliveryTypes.length() - 1));
                }

            }
            if (!CollectionUtils.isEmpty(dinnerListing.getMenuItem().getDinnerSpecialNeeds())) {
                StringBuffer specialNeeds = new StringBuffer();
                for(DinnerSpecialNeeds sn : dinnerListing.getMenuItem().getDinnerSpecialNeeds()) {
                    specialNeeds.append(sn.getName()).append(",");
                }
                if (StringUtils.hasText(specialNeeds.toString())) {
                    dinnerListing.setSpecialNeeds(specialNeeds.substring(0, specialNeeds.length() - 1));
                }
            }
            org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
            String response = placesDataService.addPlace(dinnerListing);
            JsonNode rootNode = mapper.readTree(response);
            JsonNode status = rootNode.get("status");
            if (org.apache.commons.lang.StringUtils.isNotEmpty(status.getTextValue()) && status.getTextValue().equalsIgnoreCase("OK")) {
                JsonNode placeId = rootNode.get("place_id");
                dinnerListing.setPlaceId(placeId.getTextValue());
                dinnerListing.setMarked(Boolean.TRUE);
            }
            return dinnerListingRepository.save(dinnerListing);
        } catch (P2PDinnerException p2pExcep) {
            throw p2pExcep;
        } catch (Exception excep) {
            throw exceptionMessageBuilder.createException(ErrorCode.UNKNOWN, new String[] {excep.getMessage()}, Locale.US);
        }
    }

    public Collection<DinnerListing> currentListings() {
        return  dinnerListingRepository.currentListings();
    }

    public Collection<DinnerListing> expiredMarkedListings() {
        return dinnerListingRepository.expiredListings();
    }

    public void saveListings(List<DinnerListing> listings) {
        dinnerListingRepository.save(listings);
    }

    public Collection<DinnerListing> currentListings(Integer profileId) {
        return dinnerListingRepository.currentListings(profileId);
    }

    @Transactional
    public int getOrderedQuantity(Integer listingId) {
        return dinnerCartDataService.countOrderedQuantityByListingId(listingId);
    }

    public Collection<DinnerListing> listingByDate(Integer profileId, Date listingDate) {
        DateTime startDate = new DateTime(listingDate, DateTimeZone.UTC);
        DateTime endDate = startDate.plusDays(1);
        return dinnerListingRepository.listingsByDate(profileId, 
        		new java.sql.Timestamp(startDate.toDateTime(DateTimeZone.forID("UTC")).toLocalDateTime().toDate().getTime()), 
        		new java.sql.Timestamp(endDate.toDateTime(DateTimeZone.forID("UTC")).toLocalDateTime().toDate().getTime()));
    }


    public DinnerListing listingById(Integer listingId) {
        return dinnerListingRepository.findOne(listingId);
    }
}
