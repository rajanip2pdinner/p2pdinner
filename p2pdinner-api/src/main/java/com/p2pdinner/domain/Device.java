package com.p2pdinner.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rajani@p2pdinner.com Class to store information about device related
 *         to user
 */
@Entity
@Table(name = "devices")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device implements Serializable {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "DEVICE_SEQ", allocationSize = 25)
	@Id
	@GeneratedValue(generator = "DEVICE_SEQ")
	@Column(name = "device_id")
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userprofile_id")
	@JsonIgnore
	private UserProfile userProfile;

	@Column(name = "device_type")
	private String deviceType;

	@Column(name = "registration_id")
	private String registrationId;

	@Column(name = "notifications_enabled")
	private Boolean notificationsEnabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

}
