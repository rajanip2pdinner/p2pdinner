package com.p2pdinner.entities;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by rajaniy on 5/31/16.
 */

public class AppAccessToken implements Serializable {

    private static String TAG = "P2PDinner";

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private Long expiresIn;
    @SerializedName("created_time")
    private Calendar createdTime = Calendar.getInstance();

    public Boolean isExpired() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTime expirationDateTime = new DateTime(getCreatedTime()).plusSeconds(expiresIn.intValue());
        DateTime currentDateTime = DateTime.now();
        Log.d(TAG, formatter.print(expirationDateTime));
        Log.d(TAG, "Current Time: " + formatter.print(currentDateTime));
        return expirationDateTime.isBeforeNow();
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
