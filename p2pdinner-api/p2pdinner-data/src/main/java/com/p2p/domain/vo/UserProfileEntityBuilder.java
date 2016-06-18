package com.p2p.domain.vo;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.data.repositories.DeviceRepository;
import com.p2p.domain.Device;
import com.p2p.domain.UserProfile;

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
			userProfile.setEmailAddress(rootNode.get("emailAddress").getTextValue());
		} else {
			throw new Exception("Required field emailAddress is missing");
		}
		if (rootNode.has("password")) {
			userProfile.setPassword(rootNode.get("password").getTextValue());
		} else {
			throw new Exception("Required field password is missing");
		}
		if (rootNode.has("profile_name")) {
			userProfile.setName(rootNode.get("profile_name").getTextValue());
		}
		if (rootNode.has("authentication_provider")) {
			userProfile.setAuthenticationProvider(rootNode.get("authentication_provider").getTextValue());
		}
		if (rootNode.has("devices")) {
			JsonNode devices = rootNode.get("devices");
			if (devices instanceof ArrayNode) {
				ArrayNode devicesNode = (ArrayNode) devices;
				for(JsonNode device : devicesNode) {
					Device deviceObj = new Device();
					if (device.has("deviceType")) {
						deviceObj.setDeviceType(device.get("deviceType").getTextValue());
					}
					if (device.has("registrationId")) {
						deviceObj.setRegistrationId(device.get("registrationId").getTextValue());
					}
					if (device.has("notificationsEnabled")) {
						deviceObj.setNotificationsEnabled(device.get("notificationsEnabled").getBooleanValue());
					}
					userProfile.addDevice(deviceObj);
				}
			}
		}
		return userProfile;
	}
	
}
