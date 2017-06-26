package com.p2pdinner.service;

import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.exceptions.P2PDinnerDataException;
import com.p2pdinner.exceptions.Reason;
import com.p2pdinner.repositories.DinnerCategoryRepository;
import com.p2pdinner.repositories.DinnerDeliveryRepository;
import com.p2pdinner.repositories.DinnerSpecialNeedsRepository;
import com.p2pdinner.repositories.MenuRepository;
import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.vo.Address;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.domain.vo.MenuItemVO;
import com.p2pdinner.utils.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MenuDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuDataService.class);

	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder;
	
	@Autowired
	private EntityBuilder<Address, String> addressEntityBuilder;
	
	@Autowired
	private DinnerCategoryRepository dinnerCategoryRepository;

	@Autowired
	private DinnerDeliveryRepository dinnerDeliveryRepository;

	@Autowired
	private DinnerSpecialNeedsRepository dinnerSpecialNeedsRepository;
	
	@Autowired
	private PlacesDataService placesDataService;
	
	@Autowired
	private ExceptionMessageBuilder excepBuilder;
	
	@Transactional
	public MenuItem saveOrUpdateMenuItem(MenuItemVO menuItemVO) throws Exception {
		LOGGER.info("Updating menu item {} - for user {}", menuItemVO.getTitle(), menuItemVO.getUserId());
		if (menuItemVO.getUserId() == null) {
			throw new Exception("Invalid user profile. Failed to create menu item.");
		}
		if (StringUtils.isEmpty(menuItemVO.getTitle())) {
			throw new Exception("Invalid menu item title. Failed to create menu item.");
		}
		MenuItem menu = null;
		if (menuItemVO.getId() != null) {
			menu = menuRepository.findOne(menuItemVO.getId());
			if (menu == null) {
				throw new P2PDinnerDataException(Reason.INVALID_MENU_ITEM, "Invalid menu item ");
			}
		} else {
			menu = menuRepository.findMenuByTitleAndUserProfileId(menuItemVO.getTitle(), menuItemVO.getUserId());
		}
		if ( menu == null ) {
			 menu = menuEntityBuilder.build(menuItemVO);
		} else {
			menu.getDinnerCategories().clear();
			menu.getDinnerDeliveries().clear();
			menu.getDinnerSpecialNeeds().clear();
			if (StringUtils.isNotEmpty(menuItemVO.getDinnerCategories())) {
				for (String c : menuItemVO.getDinnerCategories().split(",")) {
					menu = this.addCategory(menu, c);
				}
			}
			if (StringUtils.isNotEmpty(menuItemVO.getDinnerSpecialNeeds())) {
				for (String sn : menuItemVO.getDinnerSpecialNeeds().split(",")) {
					menu = this.addSpecialNeed(menu, sn);
				}
			}
			if (StringUtils.isNotEmpty(menuItemVO.getDinnerDelivery())) {
				for (String d : menuItemVO.getDinnerDelivery().split(",")) {
					menu = this.addDelivery(menu, d);
				}
			}
			BeanUtils.copyProperties(menuItemVO, menu, PropertyUtil.getNullPropertiesString(menuItemVO));
			String addr = StringUtils.join(new String[] {menuItemVO.getAddressLine1(), menuItemVO.getAddressLine2(), menuItemVO.getCity(), menuItemVO.getState(), menuItemVO.getZipCode()}, " ");
			String response = placesDataService.getGeoCode(addr);
			Address address = addressEntityBuilder.build(response);
			if (address != null) {
				menu.setAddressLine1(address.getStreetNumber());
				menu.setAddressLine2(address.getStreet());
				menu.setCity(address.getCity());
				menu.setZipCode(address.getPostalCode());
				menu.setState(address.getState());
			}
			
			menu.setIsActive(true);
			menu = menuRepository.save(menu);
		}
		
		return menu;
	}
	
	@Transactional
	public void deleteMenuItems(Set<Integer> menuItemIds) throws Exception {
		LOGGER.info("Deleting menu items {}", menuItemIds.toArray());
		if (menuItemIds == null || menuItemIds.isEmpty()) {
			return;
		}
		menuRepository.deleteMenuItems(menuItemIds);
	}
	
	public List<MenuItem> menuItemsByProfile(Integer profileId) {
		LOGGER.info("Fetching active menu items for profile {]", profileId);
		List<MenuItem> items = menuRepository.findMenuByProfileId(profileId);
		return items;
	}

	public MenuItem findMenuByTitle(String title, Integer profileId) {
		return menuRepository.findMenuByTitleAndUserProfileId(title, profileId);
	}
	
	public List<MenuItem> findMenuByProfileId(Integer profileId) {
		return menuRepository.findMenuByProfileId(profileId);
	}
	
	public MenuItem findMenuItemById(Integer menuItemId) {
		return menuRepository.findOne(menuItemId);
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
