package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rajaniy on 10/24/15.
 */
public class SellerListing implements Serializable {

    /*
    [
  {
    "title": "Raita",
    "description": "A cool combination of fresh whipped yogurt & herbs",
    "profile_id": 267,
    "name": "1058312690853347",
    "categories": [],
    "special_needs": [],
    "delivery": [],
    "listing": {
      "listing_id": 469,
      "cost_per_item": 2.5,
      "quantity_available": 6,
      "ordered_quantity": 0,
      "start_time": "10/24/2015 18:00:00",
      "close_time": "10/24/2015 21:30:00",
      "end_date": "10/24/2015 21:30:00"
    }
  }
]
     */
    private String title;
    private String description;
    private Integer profileId;
    private String name;
    private String[] categories;
    private String[] specialNeeds;
    private String[] delivery;
    @SerializedName("listing")
    private DinnerListing dinnerListing;

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

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String[] getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String[] specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public String[] getDelivery() {
        return delivery;
    }

    public void setDelivery(String[] delivery) {
        this.delivery = delivery;
    }

    public DinnerListing getDinnerListing() {
        return dinnerListing;
    }

    public void setDinnerListing(DinnerListing dinnerListing) {
        this.dinnerListing = dinnerListing;
    }
}
