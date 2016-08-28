package com.p2pdinner.entities;

import java.io.Serializable;

/**
 * Created by rajaniy on 12/26/15.
 */
public class DinnerListingViewContent implements Serializable{
    private Integer dinnerListingId;
    private String title;
    private String address;
    private String distance;
    private String timings;
    private String cost;
    private String profile;
    private String deliveryOptions;
    private Integer availableQuantity;
    private String imageUri;
    private String specialNeeds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(String deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public Integer getDinnerListingId() {
        return dinnerListingId;
    }

    public void setDinnerListingId(Integer dinnerListingId) {
        this.dinnerListingId = dinnerListingId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
    }
}
