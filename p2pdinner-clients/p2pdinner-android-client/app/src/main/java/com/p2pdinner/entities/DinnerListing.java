package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rajaniy on 10/22/15.
 */
public class DinnerListing implements Serializable {
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_date")
    private String endTime;
    @SerializedName("close_time")
    private String closeTime;
    @SerializedName("quantity_available")
    private Integer availableQuantity;
    private Long menuItemId;
    @SerializedName("cost_per_item")
    private Double costPerItem;
    @SerializedName("listing_id")
    private Integer dinnerListingId;
    @SerializedName("ordered_quantity")
    private Integer orderQuantity;
    @SerializedName("image_uri")
    private String imageUri;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Double getCostPerItem() {
        return costPerItem;
    }

    public void setCostPerItem(Double costPerItem) {
        this.costPerItem = costPerItem;
    }

    public Integer getDinnerListingId() {
        return dinnerListingId;
    }

    public void setDinnerListingId(Integer dinnerListingId) {
        this.dinnerListingId = dinnerListingId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
