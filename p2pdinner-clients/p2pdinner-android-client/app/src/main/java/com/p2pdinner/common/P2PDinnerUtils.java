package com.p2pdinner.common;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

/**
 * Created by rajaniy on 6/12/16.
 */

public class P2PDinnerUtils {

    private  DateTime dateTime;
    private  String dateTimeInStr;

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeInStr() {
        return dateTimeInStr;
    }

    public void setDateTimeInStr(String dateTimeInStr) {
        this.dateTimeInStr = dateTimeInStr;
    }

    private static P2PDinnerUtils instance;

    private P2PDinnerUtils() {

    }

    public static P2PDinnerUtils convert(DateTime dateTime) {
        instance = new P2PDinnerUtils();
        instance.setDateTime(dateTime);
        return instance;
    }

    public static P2PDinnerUtils convert(String dateTimeStr) {
        instance = new P2PDinnerUtils();
        instance.setDateTimeInStr(dateTimeStr);
        return instance;
    }

    public String toDateString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
        return dateTimeFormatter.print(this.dateTime);
    }

    public DateTime toDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
        return dateTimeFormatter.parseDateTime(this.dateTimeInStr);
    }

    public String toDateTimeString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss");
        return dateTimeFormatter.print(this.dateTime);
    }

    public DateTime toDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss");
        return dateTimeFormatter.parseDateTime(this.dateTimeInStr);
    }

    public DateTime toDateTimeWithUTC() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss");
        return dateTimeFormatter.withZoneUTC().parseDateTime(this.dateTimeInStr);
    }


}
