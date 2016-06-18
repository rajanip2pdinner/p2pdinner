package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rajaniy on 10/12/15.
 */
public class DinnerMenuItem implements Serializable {

    private Long id;
    private String title;
    @SerializedName("dinnerCategories")
    private String categories;
    private String description;
    private String photoTags;
    private String availableDate;

    @SerializedName("startDate")
    private String fromTime;
    @SerializedName("endDate")
    private String toTime;
    @SerializedName("closeDate")
    private String closeTime;
    @SerializedName("addressLine1")
    private String addressLine1;
    @SerializedName("addressLine2")
    private String addressLine2;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("dinnerDelivery")
    private String deliveryOptions;
    @SerializedName("costPerItem")
    private Double cost;
    @SerializedName("dinnerSpecialNeeds")
    private String specialNeeds;
    private Long profileId;
    @SerializedName("imageUri")
    private String imageUri;
    @SerializedName("quantity_available")
    private Integer availableQuantity;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_name")
    private String profileName;

    private DinnerListing dinnerListing;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DinnerListing getDinnerListing() {
        return dinnerListing;
    }

    public void setDinnerListing(DinnerListing dinnerListing) {
        this.dinnerListing = dinnerListing;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoTags() {
        return photoTags;
    }

    public void setPhotoTags(String photoTags) {
        this.photoTags = photoTags;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }


    public String getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(String deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
