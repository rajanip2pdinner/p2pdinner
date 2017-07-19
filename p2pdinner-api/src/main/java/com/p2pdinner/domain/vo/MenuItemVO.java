package com.p2pdinner.domain.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.p2pdinner.utils.JsonCalendarDeserializer;
import com.p2pdinner.utils.JsonCalendarSerializer;

import java.io.Serializable;
import java.util.Calendar;


public class MenuItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private Integer userId;
	private Boolean isActive;
	private String dinnerCategories;
	private String dinnerSpecialNeeds;
	private String dinnerDelivery;
	private Integer id;
	private String addressLine1;
	private String addressLine2;
	private String zipCode;
	private String state;
	private String city;
	private Integer availableQuantity;
	private Float costPerItem;
	private String imageUri;
	
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar startDate;
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar endDate;
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar closeDate;
	
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getDinnerCategories() {
		return dinnerCategories;
	}
	public void setDinnerCategories(String dinnerCategories) {
		this.dinnerCategories = dinnerCategories;
	}
	public String getDinnerSpecialNeeds() {
		return dinnerSpecialNeeds;
	}
	public void setDinnerSpecialNeeds(String dinnerSpecialNeeds) {
		this.dinnerSpecialNeeds = dinnerSpecialNeeds;
	}
	public String getDinnerDelivery() {
		return dinnerDelivery;
	}
	public void setDinnerDelivery(String dinnerDelivery) {
		this.dinnerDelivery = dinnerDelivery;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer menuItemId) {
		this.id = menuItemId;
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
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
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
