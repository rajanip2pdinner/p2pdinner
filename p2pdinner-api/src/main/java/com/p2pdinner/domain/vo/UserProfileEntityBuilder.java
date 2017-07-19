package com.p2pdinner.domain.vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.p2pdinner.repositories.DeviceRepository;
import com.p2pdinner.domain.Device;
import com.p2pdinner.domain.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

/**
 * 
 * @author rajaniy
 * Converts request to @see UserProfile entity
 */
public class UserProfileEntityBuilder implements EntityBuilder<UserProfile, String> {
	
	@Autowired
	private DeviceRepository deviceRepository;
	@Transactional
	public UserProfile build(String r) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = new UserProfile();
		JsonNode rootNode = mapper.readTree(r);
		if (rootNode.has("emailAddress")) {
			userProfile.setEmailAddress(rootNode.get("emailAddress").asText());
		} else {
			throw new Exception("Required field emailAddress is missing");
		}
		if (rootNode.has("password")) {
			userProfile.setPassword(rootNode.get("password").asText());
		} else {
			throw new Exception("Required field password is missing");
		}
		if (rootNode.has("profile_name")) {
			userProfile.setName(rootNode.get("profile_name").asText());
		}
		if (rootNode.has("authentication_provider")) {
			userProfile.setAuthenticationProvider(rootNode.get("authentication_provider").asText());
		}
		if (rootNode.has("devices")) {
			JsonNode devices = rootNode.get("devices");
			if (devices instanceof ArrayNode) {
				ArrayNode devicesNode = (ArrayNode) devices;
				for(JsonNode device : devicesNode) {
					Device deviceObj = new Device();
					if (device.has("deviceType")) {
						deviceObj.setDeviceType(device.get("deviceType").asText());
					}
					if (device.has("registrationId")) {
						deviceObj.setRegistrationId(device.get("registrationId").asText());
					}
					if (device.has("notificationsEnabled")) {
						deviceObj.setNotificationsEnabled(device.get("notificationsEnabled").asBoolean());
					}
					userProfile.addDevice(deviceObj);
				}
			}
		}
		return userProfile;
	}
	
}
