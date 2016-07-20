package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rajaniy on 1/17/16.
 */
public class Order implements Serializable {
    /*
        {
      "name": "1058312690853347",
      "description": "Vegetable Biriyani",
      "title": "Vegetable Biriyani",
      "total_price": 5.49,
      "order_quantity": 1,
      "delivery_type": "",
      "pass_code": "AhginF1S",
      "cost_per_item": 5.49
    },
     */
    @SerializedName("name")
    private String profileName;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;
    @SerializedName("total_price")
    private Double totalPrice;
    @SerializedName("order_quantity")
    private Integer orderQuantity;
    @SerializedName("delivery_type")
    private String deliveryType;
    @SerializedName("pass_code")
    private String passCode;
    @SerializedName("cost_per_item")
    private Double costPerItem;
    @SerializedName("address_line1")
    private String addressLine1;
    @SerializedName("address_line2")
    private String addressLine2;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("start_time")
    private long startTime;
    @SerializedName("end_time")
    private long endTime;
    @SerializedName("close_time")
    private long closeTime;
    @SerializedName("image_uri")
    private String imageUri;
    @SerializedName("buyer_rating")
    private Integer buyerRating;
    @SerializedName("seller_rating")
    private Integer sellerRating;
    @SerializedName("cart_id")
    private Integer cartId;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public Double getCostPerItem() {
        return costPerItem;
    }

    public void setCostPerItem(Double costPerItem) {
        this.costPerItem = costPerItem;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getBuyerRating() {
        return buyerRating;
    }

    public void setBuyerRating(Integer buyerRating) {
        this.buyerRating = buyerRating;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
