/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.domain.vo;

import java.io.Serializable;

/**
 *
 * @author rajani
 */
public class DinnerListingVO implements Serializable {
    private String startTime;
    private String endTime;
    private String closeTime;
    private Integer availableQuantity;
    private Integer menuItemId;
    private Double costPerItem;
    private Integer dinnerListingId;

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

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Double getCostPerItem() {
        return costPerItem;
    }

    public void setCostPerItem(Double costPerItem) {
        this.costPerItem = costPerItem;
    }
    
    public String getCloseTime() {
		return closeTime;
	}
    
    public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

    public Integer getDinnerListingId() {
        return dinnerListingId;
    }

    public void setDinnerListingId(Integer dinnerListingId) {
        this.dinnerListingId = dinnerListingId;
    }
}
