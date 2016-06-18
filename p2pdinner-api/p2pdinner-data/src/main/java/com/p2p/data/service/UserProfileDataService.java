/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2p.data.service;

import java.util.*;

import javax.transaction.Transactional;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.domain.Device;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.p2p.data.repositories.StateRepository;
import com.p2p.data.repositories.UserProfileRepository;
import com.p2p.domain.State;
import com.p2p.domain.UserProfile;
import com.p2p.domain.vo.Address;
import com.p2p.domain.vo.EntityBuilder;
import com.p2p.utils.CommonUtils;

/**
 *
 * @author rajani
 */
@Component
public class UserProfileDataService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserProfileDataService.class);

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private PlacesDataService placesDataService;
	
	@Autowired
	private EntityBuilder<Address,String> addressEntityBuilder;
	
	@Autowired
	private EntityBuilder<UserProfile, String> userProfileEntityBuilder;

	@Autowired
	private ExceptionMessageBuilder exceptionMessageBuilder;
	
	@Transactional(rollbackOn = Exception.class)
	public UserProfile saveProfile(String request) throws Exception {
		UserProfile userProfile = userProfileEntityBuilder.build(request);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving object user profile {}",
					userProfile.getEmailAddress());
		}
		
		UserProfile u = userProfileRepository
				.findUserProfileByEmailAddress(userProfile.getEmailAddress());
		if (u == null) {
			userProfile.setPassword(CommonUtils.getHashValue(userProfile.getPassword()));
			u = userProfile;
			u.setName(userProfile.getName());
			u.setIsActive(Boolean.TRUE);
		} else {
			//BeanUtils.copyProperties(userProfile, u, "id");
			u.setPassword(CommonUtils.getHashValue(userProfile.getPassword()));
			u.setName(userProfile.getName());
			u.setIsActive(Boolean.TRUE);
		}
		String addr = StringUtils.join(new String[] {u.getAddressLine1(), u.getAddressLine2(), u.getCity(), u.getState(), u.getZip()}, " ");
		String response = placesDataService.getGeoCode(addr);
		Address address = addressEntityBuilder.build(response);
		if (address != null) {
			u.setAddressLine1(address.getStreetNumber());
			u.setAddressLine2(address.getStreet());
			u.setCity(address.getCity());
			u.setZip(address.getPostalCode()); // + "-" + address.getPostalCodeSuffix());
			u.setLatitude(address.getLatitude());
			u.setLongitude(address.getLongitude());
		}
		return userProfileRepository.save(u);
	}

	public UserProfile findUserProfileByEmailAddressAndPassword(
			String emailAddress, String password) throws Exception {
		String hp = CommonUtils.getHashValue(password);
		LOGGER.info("Email Address => {}, Password => {}", emailAddress, hp);
		return userProfileRepository.findUserProfileByEmailAddressAndPassword(emailAddress, hp);
	}
	
	@Transactional
	public UserProfile activateProfile(Integer id) {
		LOGGER.info("Activating profile {}", id);
		UserProfile userProfile = userProfileRepository.findOne(id);
		userProfile.setIsActive(Boolean.TRUE);
		return userProfileRepository.saveAndFlush(userProfile);
	}
	
	@Transactional
	public UserProfile deactivateProfile(Integer id) {
		LOGGER.info("Deactivating profile {}", id);
		UserProfile userProfile = userProfileRepository.findOne(id);
		userProfile.setIsActive(Boolean.FALSE);
		return userProfileRepository.saveAndFlush(userProfile);
	}

	public Collection<State> getAllStates() {
		LOGGER.info("Getting all states....");
		return stateRepository.findAll();
	}
	
	private Double getLatitude(String response) throws Exception {
		Double lat = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(response);
			JsonNode results = rootNode.get("results");
			if (results.isArray()) {
				Iterator<JsonNode> iterator = results.iterator();
				JsonNode childNode= iterator.next();
				JsonNode latitudeNode = childNode.path("geometry").path("location").path("lat");
				lat = latitudeNode.asDouble();
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
			JsonNode results = rootNode.get("results");
			if (results.isArray()) {
				Iterator<JsonNode> iterator = results.iterator();
				JsonNode childNode= iterator.next();
				JsonNode latitudeNode = childNode.path("geometry").path("location").path("lng");
				lng = latitudeNode.asDouble();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return lng;
	}
	
	private Boolean isPartialMatch(String response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(response);
			JsonNode results = rootNode.get("results");
			if (results.isArray()) {
				Iterator<JsonNode> iterator = results.iterator();
				JsonNode childNode= iterator.next();
				if (childNode.has("partial_match")) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
		}
		return false;
	}

	public UserProfile getProfileById(Integer id) {
		return userProfileRepository.findOne(id);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void saveProfile(UserProfile profile) {
		userProfileRepository.save(profile);
	}


	@Transactional(rollbackOn = Exception.class)
	public void addDevice(Integer id, Device device) throws P2PDinnerException {
		try {
			UserProfile profile = getProfileById(id);
			if (profile != null) {
				profile.addDevice(device);
				userProfileRepository.saveAndFlush(profile);
			}
		}
		catch (Exception excep) {
			throw exceptionMessageBuilder.createException(ErrorCode.UNKNOWN, new String[] {excep.getMessage()}, Locale.US);
		}
	}
	
}
