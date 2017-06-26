package com.p2pdinner.domain.vo;

import com.p2pdinner.repositories.*;
import com.p2pdinner.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.HashSet;

public class MenuItemEntityBuilder implements EntityBuilder<MenuItem, MenuItemVO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemEntityBuilder.class);

	@Autowired
	private DinnerCategoryRepository dinnerCategoryRepository;

	@Autowired
	private DinnerDeliveryRepository dinnerDeliveryRepository;

	@Autowired
	private DinnerSpecialNeedsRepository dinnerSpecialNeedsRepository;

	@Autowired
	private DinnerListingRepository dinnerListingRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private SimpleDateFormat simpleDateFormat;
	
	@Autowired
	private MenuRepository menuRepository;

	@Transactional
	public MenuItem build(MenuItemVO r) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		LOGGER.info("Converting view object to entity object");
		MenuItem listing = new MenuItem();
		BeanUtils.copyProperties(r, listing);
		if (StringUtils.isNotEmpty(r.getDinnerCategories())) {
			for (String c : r.getDinnerCategories().split(",")) {
				listing = this.addCategory(listing, c);
			}
		}
		if (StringUtils.isNotEmpty(r.getDinnerSpecialNeeds())) {
			for (String sn : r.getDinnerSpecialNeeds().split(",")) {
				listing = this.addSpecialNeed(listing, sn);
			}
		}
		if (StringUtils.isNotEmpty(r.getDinnerDelivery())) {
			for (String d : r.getDinnerDelivery().split(",")) {
				listing = this.addDelivery(listing, d);
			}
		}
		UserProfile userProfile = userProfileRepository.findOne(r.getUserId());
		if (userProfile == null) {
			throw new Exception("Invalid profile.");
		}
		userProfile.addMenuItem(listing);
		listing.setIsActive(true);
		listing.setId(null);
		listing.setStartDate(r.getStartDate());
		listing.setEndDate(r.getEndDate());
		listing.setCloseDate(r.getCloseDate());
		listing = menuRepository.save(listing);
		return listing;
	}

	public MenuItem addCategory(MenuItem dinnerListing, String category) {
		if (dinnerListing.getDinnerCategories() == null) {
			dinnerListing.setDinnerCategories(new HashSet<DinnerCategory>());
		} 
		DinnerCategory c = dinnerCategoryRepository.findOneByName(category);
		if (c != null) {
			dinnerListing.getDinnerCategories().add(c);
		}
		return dinnerListing;
	}

	public MenuItem addDelivery(MenuItem dinnerListing, String delivery) {
		if (dinnerListing.getDinnerDeliveries() == null) {
			dinnerListing.setDinnerDeliveries(new HashSet<DinnerDelivery>());
		} 
		DinnerDelivery d = dinnerDeliveryRepository.findOneByName(delivery);
		if (d != null) {
			dinnerListing.getDinnerDeliveries().add(d);
		}
		return dinnerListing;
	}

	public MenuItem addSpecialNeed(MenuItem dinnerListing, String specialNeed) {
		if (dinnerListing.getDinnerSpecialNeeds() == null) {
			dinnerListing.setDinnerSpecialNeeds(new HashSet<DinnerSpecialNeeds>());
		} 
		DinnerSpecialNeeds n = dinnerSpecialNeedsRepository.findOneByName(specialNeed);
		if (n != null) {
			dinnerListing.getDinnerSpecialNeeds().add(n);
		}
		return dinnerListing;
	}
}
