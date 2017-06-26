package com.p2pdinner.common.notifications;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rajaniy on 4/20/16.
 */
public class NotificationMessage implements Serializable {
    private Timestamp startDate;
    private Timestamp endDate;
    private String address;
    private String confirmationCode;
    private Double price;
    private NotificationTarget notificationTarget;
    private Integer orderQuantity;
    private String title;

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

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void setNotificationTarget(NotificationTarget notificationTarget) {
        this.notificationTarget = notificationTarget;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
