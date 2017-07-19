package com.p2pdinner.service;

import com.p2pdinner.repositories.*;
import com.p2pdinner.domain.*;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.domain.vo.MenuItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

@Component
public class P2PDataService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(P2PDataService.class);

	@Autowired
	private KeyValueRepository keyValueRepository;

	@Autowired
	private DinnerCategoryRepository dinnerCategoryRepository;

	@Autowired
	private DinnerDeliveryRepository dinnerDeliveryRepository;

	@Autowired
	private DinnerSpecialNeedsRepository dinnerSpecialNeedsRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder;

	@Transactional
	public KeyValue saveKeyValue(KeyValue keyValue) {
		KeyValue kv = keyValueRepository.findOneByKey(keyValue.getKey());
		if (kv == null) {
			kv = keyValueRepository.saveAndFlush(keyValue);
		} else {
			kv.setValue(keyValue.getValue());
			kv = keyValueRepository.saveAndFlush(keyValue);
		}
		return kv;
	}

	public Collection<KeyValue> findAllKeyValues() {
		return keyValueRepository.findAll();
	}

	@Transactional
	public DinnerCategory saveDinnerCategory(DinnerCategory dinnerCategory) {
		Calendar calendar = Calendar.getInstance();
		if (dinnerCategory == null) {
			return null;
		}
		LOGGER.info("Saving dinner category : {}", dinnerCategory.getName());
		DinnerCategory category = dinnerCategoryRepository
				.findOneByName(dinnerCategory.getName());
		if (category == null) {
			category = dinnerCategory;
			category.setIsActive(Boolean.TRUE);
			category.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			category.setStartDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			category = dinnerCategory;
		} else {
			category.setIsActive(dinnerCategory.getIsActive());
			category.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			if (category.getIsActive() != null && !category.getIsActive()) {
				category.setEndDate(new java.sql.Timestamp(calendar
						.getTimeInMillis()));
			}
		}
		return dinnerCategoryRepository.saveAndFlush(category);
	}

	@Transactional
	public Collection<DinnerCategory> getAllDinnerCategories() {
		LOGGER.info("Retrieving all dinner categories...");
		return dinnerCategoryRepository.findAll();
	}

	public DinnerCategory dinnerCategoryByName(String name) {
		return dinnerCategoryRepository.findOneByName(name);
	}

	@Transactional
	public DinnerDelivery saveDinnerDelivery(DinnerDelivery dinnerDelivery) {
		Calendar calendar = Calendar.getInstance();
		if (dinnerDelivery == null) {
			return null;
		}
		LOGGER.info("Saving dinner delivery : {}", dinnerDelivery.getName());
		DinnerDelivery category = dinnerDeliveryRepository
				.findOneByName(dinnerDelivery.getName());
		if (category == null) {
			dinnerDelivery.setIsActive(Boolean.TRUE);
			dinnerDelivery.setStartDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			dinnerDelivery.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			dinnerDelivery.setIsActive(Boolean.TRUE);
			category = dinnerDelivery;
		} else {
			category.setIsActive(dinnerDelivery.getIsActive());
			category.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			if (category.getIsActive() != null && !category.getIsActive()) {
				category.setEndDate(new java.sql.Timestamp(calendar
						.getTimeInMillis()));
			}
		}
		return dinnerDeliveryRepository.saveAndFlush(category);
	}

	public Collection<DinnerDelivery> getAllDinnerDeliveries() {
		LOGGER.info("Retrieving all dinner delieveries...");
		return dinnerDeliveryRepository.findByIsActiveTrue();
	}

	public DinnerDelivery dinnerDeliveryByName(String name) {
		return dinnerDeliveryRepository.findOneByName(name);
	}

	@Transactional
	public DinnerSpecialNeeds saveDinnerSpecialNeed(
			DinnerSpecialNeeds dinnerSpecialNeeds) {
		Calendar calendar = Calendar.getInstance();
		if (dinnerSpecialNeeds == null) {
			return null;
		}
		LOGGER.info("Saving dinner delivery : {}", dinnerSpecialNeeds.getName());
		DinnerSpecialNeeds need = dinnerSpecialNeedsRepository
				.findOneByName(dinnerSpecialNeeds.getName());
		if (need == null) {
			dinnerSpecialNeeds.setIsActive(Boolean.TRUE);
			dinnerSpecialNeeds.setStartDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			dinnerSpecialNeeds.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			dinnerSpecialNeeds.setIsActive(Boolean.TRUE);
			need = dinnerSpecialNeeds;
		} else {
			need.setIsActive(dinnerSpecialNeeds.getIsActive());
			need.setModifiedDate(new java.sql.Timestamp(calendar
					.getTimeInMillis()));
			if (!need.getIsActive()) {
				need.setEndDate(new java.sql.Timestamp(calendar
						.getTimeInMillis()));
			}
		}
		return dinnerSpecialNeedsRepository.saveAndFlush(need);
	}

	public Collection<DinnerSpecialNeeds> getAllDinnerSpecialNeeds() {
		LOGGER.info("Retrieving all dinner special needs...");
		return dinnerSpecialNeedsRepository.findAll();
	}

	public DinnerSpecialNeeds dinnerSpecialNeedsByName(String name) {
		return dinnerSpecialNeedsRepository.findOneByName(name);
	}

	public Collection<MenuItem> getAllListings() {
		return menuRepository.findAll();
	}

	@Transactional
	public Collection<MenuItem> getCurrentListings() {
		return Collections.EMPTY_LIST;
	}
}
