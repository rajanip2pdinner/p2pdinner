package com.p2p.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.p2p.utils.JsonDinnerListingSerializer;
import com.p2p.utils.JsonTimestampSerializer;


@Entity
@Table(name = "dinner_listing")
@JsonSerialize(using = JsonDinnerListingSerializer.class)
public class DinnerListing implements Serializable {
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "DINNER_LISTING_SEQ", allocationSize = 25)
	@Id
	@GeneratedValue(generator = "DINNER_LISTING_SEQ")
	private Integer id;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name = "start_time")
	private java.sql.Timestamp startTime;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name = "end_time")
	private java.sql.Timestamp endTime;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name = "close_time")
	private java.sql.Timestamp closeTime;

	@Column(name = "available_quantity")
	private Integer availableQuantity;

	@Column(name = "ordered_quantity")
	private Integer orderQuantity = 0;
	
	@OneToOne
    @JoinColumn(name = "menu_item_id")
	private com.p2p.domain.MenuItem menuItem;
	
	@Column(name = "cost_per_item")
	private Double costPerItem;
	
	@Column(name = "place_id")
	private String placeId;
	
	@Column(name = "marked")
	private Boolean marked;
	
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

	@Column(name = "categories")
	private String categories;

	@Column(name = "special_needs")
	private String specialNeeds;

	@Column(name = "delivery_types")
	private String deliveryTypes;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.sql.Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(java.sql.Timestamp startTime) {
		this.startTime = startTime;
	}

	public java.sql.Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(java.sql.Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}
	public MenuItem getMenuItem() {
		return menuItem;
	}
	
	public Integer getAvailableQuantity() {
		return availableQuantity;
	}
	
	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public java.sql.Timestamp getCloseTime() {
		return this.closeTime;
	}
	
	public void setCloseTime(java.sql.Timestamp closeTime) {
		this.closeTime = closeTime;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	public void setCostPerItem(Double costPerItem) {
		this.costPerItem = costPerItem;
	}
	
	public Double getCostPerItem() {
		return costPerItem;
	}
	
	public void setMarked(Boolean marked) {
		this.marked = marked;
	}
	
	public Boolean getMarked() {
		return marked;
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


	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getSpecialNeeds() {
		return specialNeeds;
	}

	public void setSpecialNeeds(String specialNeeds) {
		this.specialNeeds = specialNeeds;
	}

	public String getDeliveryTypes() {
		return deliveryTypes;
	}

	public void setDeliveryTypes(String deliveryTypes) {
		this.deliveryTypes = deliveryTypes;
	}
}
