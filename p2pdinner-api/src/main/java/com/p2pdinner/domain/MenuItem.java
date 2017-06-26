/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.p2pdinner.utils.JsonCalendarSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author rajani
 */
@Entity
@Table(name = "menu_items")
public class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "MENU_SEQ", allocationSize = 25)
	@Id
	@GeneratedValue(generator = "MENU_SEQ")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "is_active")
	private Boolean isActive;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "dinner_listing_category", joinColumns = @JoinColumn(name = "dinner_listing_id"), inverseJoinColumns = @JoinColumn(name = "dinner_category_id"))
	private Set<DinnerCategory> dinnerCategories = new HashSet<DinnerCategory>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "dinner_listing_special_needs", joinColumns = @JoinColumn(name = "dinner_listing_id"), inverseJoinColumns = @JoinColumn(name = "dinner_special_needs_id"))
	private Set<DinnerSpecialNeeds> dinnerSpecialNeeds = new HashSet<DinnerSpecialNeeds>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "dinner_listing_delivery", joinColumns = @JoinColumn(name = "dinner_listing_id"), inverseJoinColumns = @JoinColumn(name = "dinner_delivery_id"))
	private Set<DinnerDelivery> dinnerDeliveries = new HashSet<DinnerDelivery>();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userprofile_id")
	private UserProfile userProfile;
	
	@OneToMany
	@JoinColumn(name = "menu_item_id")
	@JsonIgnore
	private List<DinnerListing> dinnerListings;
	
	@Column(name = "address_line1")
	private String addressLine1;
	
	@Column(name = "address_line2")
	private String addressLine2;
	
	@Column(name = "zip_code")
	private String zipCode;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "available_quantity")
	private Integer availableQuantity;
	
	@Column(name = "cost_per_item")
	private Float costPerItem;
	
	@Column(name = "image_uri")
	private String imageUri;
	
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar startDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar endDate;
	
	@Column(name = "close_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar closeDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userProfile == null) ? 0 : userProfile.hashCode());
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
		MenuItem other = (MenuItem) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userProfile == null) {
			if (other.userProfile != null)
				return false;
		} else if (!userProfile.equals(other.userProfile))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Collection<DinnerCategory> getDinnerCategories() {
		return dinnerCategories;
	}

	public void setDinnerCategories(HashSet<DinnerCategory> dinnerCategories) {
		this.dinnerCategories = dinnerCategories;
	}

	public Collection<DinnerSpecialNeeds> getDinnerSpecialNeeds() {
		return dinnerSpecialNeeds;
	}

	public void setDinnerSpecialNeeds(HashSet<DinnerSpecialNeeds> dinnerSpecialNeeds) {
		this.dinnerSpecialNeeds = dinnerSpecialNeeds;
	}

	public Collection<DinnerDelivery> getDinnerDeliveries() {
		return dinnerDeliveries;
	}

	public void setDinnerDeliveries(HashSet<DinnerDelivery> dinnerDeliveries) {
		this.dinnerDeliveries = dinnerDeliveries;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	public List<DinnerListing> getDinnerListings() {
		return dinnerListings;
	}
	
	public void setDinnerListings(List<DinnerListing> dinnerListings) {
		this.dinnerListings = dinnerListings;
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
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public Float getCostPerItem() {
		return costPerItem;
	}
	public void setCostPerItem(Float costPerItem) {
		this.costPerItem = costPerItem;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageFileName) {
		this.imageUri = imageFileName;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Calendar closeDate) {
		this.closeDate = closeDate;
	}
}
