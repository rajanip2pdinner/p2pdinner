package com.p2p.common.notifications;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rajaniy on 4/20/16.
 */
public class NotificationMessage implements Serializable {
    private java.sql.Timestamp startDate;
    private java.sql.Timestamp endDate;
    private String address;
    private String confirmationCode;
    private Double price;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
