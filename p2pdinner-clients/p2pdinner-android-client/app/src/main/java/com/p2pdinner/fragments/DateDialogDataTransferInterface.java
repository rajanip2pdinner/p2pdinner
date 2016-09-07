package com.p2pdinner.fragments;

import android.view.View;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by rajaniy on 10/8/15.
 */
public interface DateDialogDataTransferInterface {
    void setSelectedDate(int resourceId, DateTime dateTime);
}
