package com.p2pdinner.restclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pdinner.common.Constants;
import com.p2pdinner.deserializers.DinnerMenuItemDeserializer;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.DinnerSearchResults;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by rajaniy on 10/7/15.
 */
public class PlacesServiceManager {
    private static String TAG = PlacesServiceManager.class.getName();
    private RestTemplate restTemplate = null;

    public PlacesServiceManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Observable<DinnerSearchResults> searchDinnerByLocation(final String address, final DateTime startTime, final int guests, final Locale locale) {
        return Observable.create(new Observable.OnSubscribe<DinnerSearchResults>() {
            @Override
            public void call(Subscriber<? super DinnerSearchResults> subscriber) {
                Log.d(TAG, "Requesting dinner for address ==> " + address);
                StringBuffer searchQry = new StringBuffer("");
                searchQry.append("close_time::").append(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC().print(startTime));
                searchQry.append("|guests::").append(guests);
                UriComponents uriComponents = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI)
                        .path("/places/nearbysearch")
                        .queryParam("address", address)
                        .queryParam("q", searchQry.toString())
                        .queryParam("locale", locale.toString())
                        .build();
                try {
                    String response = restTemplate.getForObject(uriComponents.toUri(), String.class);
                    Gson gson = new GsonBuilder().registerTypeAdapter(DinnerMenuItem.class, new DinnerMenuItemDeserializer()).create();
                    DinnerSearchResults results = gson.fromJson(response, DinnerSearchResults.class);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                   subscriber.onError(e);
                }
            }
        });
    }

}
