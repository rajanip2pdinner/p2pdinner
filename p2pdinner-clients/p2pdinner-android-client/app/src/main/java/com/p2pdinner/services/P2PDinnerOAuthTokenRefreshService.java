package com.p2pdinner.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.p2pdinner.P2PDinnerApplication;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.AppAccessToken;
import com.p2pdinner.restclient.UserProfileManager;

import org.springframework.util.StringUtils;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class P2PDinnerOAuthTokenRefreshService extends IntentService {

    private static final String TAG = "P2PDinnerOAuth";

    private SharedPreferences sharedPreferences = null;
    private static final int POLL_INTERVAL = 60 * 1000;

    @Inject
    UserProfileManager userProfileManager;

    private static Integer jobId = 0;

    private ComponentName serviceComponent;

    private Gson gson = new GsonBuilder().create();

    public P2PDinnerOAuthTokenRefreshService() {
        super(TAG);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ((P2PDinnerApplication) getApplication()).inject(this);
        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        final String appAccessTokenJson = sharedPreferences.getString(Constants.P2PDINNER_API_TOKEN, "");
        if (StringUtils.hasText(appAccessTokenJson)) {
            AppAccessToken appAccessToken = gson.fromJson(appAccessTokenJson, AppAccessToken.class);
            if (appAccessToken.isExpired()) {
                Log.i(TAG, "Token expired. refreshing...");
                refreshAccessToken(gson, appAccessToken);
            }
        } else {
            Log.i(TAG, "No token found. Requesting a new token...");
            requestAccesToken(gson);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Checking token validity....");
        final String appAccessTokenJson = sharedPreferences.getString(Constants.P2PDINNER_API_TOKEN, "");
        if (StringUtils.hasText(appAccessTokenJson)) {
            AppAccessToken appAccessToken = gson.fromJson(appAccessTokenJson, AppAccessToken.class);
            if (appAccessToken.isExpired()) {
                Log.i(TAG, "Token expired. refreshing...");
                refreshAccessToken(gson, appAccessToken);
            }
        } else {
            Log.i(TAG, "No token found. Requesting a new token...");
            requestAccesToken(gson);
        }
    }

    private void refreshAccessToken(final Gson gson, AppAccessToken appAccessToken) {
        userProfileManager.refreshAccessToken(appAccessToken.getRefreshToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<AppAccessToken>() {
                    private AppAccessToken appAccessToken;

                    @Override
                    public void onCompleted() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.P2PDINNER_API_TOKEN, gson.toJson(appAccessToken));
                        editor.apply();
                        editor.commit();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AppAccessToken appAccessToken) {
                        this.appAccessToken = appAccessToken;
                    }
                });
    }

    private void requestAccesToken(final Gson gson) {
        userProfileManager.requestAccessToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<AppAccessToken>() {
                    private AppAccessToken appAccessToken;

                    @Override
                    public void onCompleted() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.P2PDINNER_API_TOKEN, gson.toJson(appAccessToken));
                        editor.apply();
                        editor.commit();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AppAccessToken appAccessToken) {
                        this.appAccessToken = appAccessToken;
                    }
                });
    }

}
