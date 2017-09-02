package com.p2pdinner.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by rajaniy on 9/2/17.
 */

public class SearchCriteria implements Serializable {
    private String address;
    private DateTime startTime;
    private int guests;
    private Double maxPrice = 100d;
    private Double minPrice = 0d;

    public SearchCriteria(String address, DateTime startTime, int guests, Double maxPrice, Double minPrice) {
        this.address = address;
        this.startTime = startTime;
        this.guests = guests;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public SearchCriteria(String address, DateTime startTime, int guests) {
        this.address = address;
        this.startTime = startTime;
        this.guests = guests;
    }

    public String getAddress() {
        return address;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public int getGuests() {
        return guests;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }
}
