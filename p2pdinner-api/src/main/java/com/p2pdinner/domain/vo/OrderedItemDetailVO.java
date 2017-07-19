package com.p2pdinner.domain.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by rajaniy on 8/6/15.
 */
public class OrderedItemDetailVO implements Serializable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("total_price")
    private Double totalPrice;
    @JsonProperty("order_quantity")
    private Integer orderQuantity;
    @JsonProperty("description")
    private String description;
    @JsonProperty("title")
    private String title;
    @JsonProperty("delivery_type")
    private String deliveryType;
    @JsonProperty("pass_code")
    private String passCode;
    @JsonProperty("cost_per_item")
    private Double costPerItem;
    @JsonProperty("address_line1")
    private String addressLine1;
    @JsonProperty("address_line2")
    private String addressLine2;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("start_time")
    private java.util.Date startTime;
    @JsonProperty("end_time")
    private java.util.Date endTime;
    @JsonProperty("close_time")
    private java.util.Date closeTime;
    @JsonProperty("image_uri")
    private String imageUri;
    @JsonProperty("seller_rating")
    private Integer sellerRating;
    @JsonProperty("buyer_rating")
    private Integer buyerRating;
    @JsonProperty("cart_id")
    private Integer cartId;


    public OrderedItemDetailVO(String name, Double totalPrice, Integer orderQuantity, String description, String title, String passCode, Double costPerItem, Integer buyerRating, Integer sellerRating, Integer cartId) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
        this.description = description;
        this.title = title;
        this.deliveryType = "";
        this.passCode = passCode;
        this.costPerItem = costPerItem;
        this.buyerRating = buyerRating;
        this.sellerRating = sellerRating;
        this.cartId = cartId;
    }

    public OrderedItemDetailVO(String name, Double totalPrice, Integer orderQuantity, String description, String title, String passCode, Double costPerItem, String addressLine1, String addressLine2, String city, String state, String imageUri, Integer buyerRating, Integer sellerRating, Integer cartId) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
        this.description = description;
        this.title = title;
        this.deliveryType = "";
        this.passCode = passCode;
        this.costPerItem = costPerItem;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.imageUri = imageUri;
        this.buyerRating = buyerRating;
        this.sellerRating = sellerRating;
        this.cartId = cartId;

    }

    public OrderedItemDetailVO(String name,
                               Double totalPrice,
                               Integer orderQuantity,
                               String description,
                               String title,
                               String passCode,
                               Double costPerItem,
                               String addressLine1,
                               String addressLine2,
                               String city,
                               String state,
                               java.util.Date startTime,
                               java.util.Date endTime,
                               java.util.Date closeTime,
                               String imageUri, Integer buyerRating, Integer sellerRating, Integer cartId) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
        this.description = description;
        this.title = title;
        this.deliveryType = "";
        this.passCode = passCode;
        this.costPerItem = costPerItem;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closeTime = closeTime;
        this.imageUri = imageUri;
        this.buyerRating = buyerRating;
        this.sellerRating = sellerRating;
        this.cartId = cartId;
    }

    public OrderedItemDetailVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public java.util.Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(java.util.Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

    public Integer getBuyerRating() {
        return buyerRating;
    }

    public void setBuyerRating(Integer buyerRating) {
        this.buyerRating = buyerRating;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
