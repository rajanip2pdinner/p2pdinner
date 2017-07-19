/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dheemu
 */
@Entity
@Table(name = "user_profile")
//@JsonSerialize(using = JsonUserProfilerSerializer.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile implements Serializable {
    
    @SequenceGenerator(name = "USER_PROFILE_SEQ_GEN", allocationSize = 25)
    @GeneratedValue(generator = "USER_PROFILE_SEQ_GEN")
    @Id
    private Integer id;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "profile_name", columnDefinition = "varchar(255) default 'UNKNOWN'")
    private String name;

    @Column(name = "authentication_provider", columnDefinition = "varchar(255) default 'APP'")
    @JsonIgnore
    private String authenticationProvider;
    
    @Column(name = "address_line1")
    private String addressLine1;
    
    @Column(name = "address_line2")
    private String addressLine2;
    
    @Column(name = "city")
    private String city;
    
    @Column(name ="state")
    private String state;
    
    @Column(name = "zip")
    private String zip;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "account_balance")
    private Double accountBalance;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile")
    @JsonIgnore
    private List<MenuItem> menuItems;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Device> devices;
    
    // stripe attributes
    @JsonIgnore
    @Column(name = "token_type")
    private String tokenType;
    
    @JsonIgnore
    @Column(name = "stripe_publishable_key")
    private String stripePublishableKey;
    
    @JsonIgnore
    @Column(name = "scope")
    private String scope;
    
    @JsonIgnore
    @Column(name = "livemode")
    private Boolean liveMode;
    
    @JsonIgnore
    @Column(name = "refresh_token")
    private String refreshToken;
    
    @JsonIgnore
    @Column(name = "access_token")
    private String accessToken;

    
    @OneToOne
    private Tier tier;
    
    @JsonIgnore
    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "certificates")
    private String certificates;
    
    public String getStripeCustomerId() {
		return stripeCustomerId;
	}
    
    public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
 

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public List<MenuItem> getMenuItems() {
    	if ( menuItems == null) {
    		menuItems = new ArrayList<MenuItem>();
    	}
		return menuItems;
	}
    
    public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
    
    public void addMenuItem(MenuItem menuItem) {
    	getMenuItems().add(menuItem);
    	menuItem.setUserProfile(this);
    }
    
    public void removeMenuItem(MenuItem menuItem) {
    	getMenuItems().remove(menuItem);
    }
    
    public List<Device> getDevices(){
    	if ( devices == null) {
    		devices = new ArrayList<Device>();
    	}
    	return devices;
    }
    
    public void setDevices(List<Device> devices) {
    	this.devices = devices;
    }
    
    public void addDevice(Device device){
    	getDevices().add(device);
    	device.setUserProfile(this);
    }
    
    public void removeMenuItem(Device device) {
    	getDevices().remove(device);
    }
    
    public Tier getTier() {
		return tier;
	}
    
    public void setTier(Tier tier) {
		this.tier = tier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getStripePublishableKey() {
		return stripePublishableKey;
	}

	public void setStripePublishableKey(String stripePublishableKey) {
		this.stripePublishableKey = stripePublishableKey;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Boolean getLiveMode() {
		return liveMode;
	}

	public void setLiveMode(Boolean liveMode) {
		this.liveMode = liveMode;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(String authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }
}
