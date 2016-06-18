package com.p2pdinner.restclient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.SellerListing;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by rajaniy on 10/4/15.
 */
public class DinnerListingManager {
    private static String TAG = DinnerListingManager.class.getName();

    private RestTemplate restTemplate;

    public DinnerListingManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Observable<String[]> categories() {
        return Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/dinnercategory").build();
                String response = restTemplate.getForObject(components.toUri(), String.class);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> categories = new ArrayList<>();
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        JSONObject object = jsonArray.getJSONObject(idx);
                        categories.add(object.getString("name"));
                    }
                    String[] cs = new String[categories.size()];
                    cs = categories.toArray(cs);
                    subscriber.onNext(categories.toArray(cs));
                    subscriber.onCompleted();
                } catch (Exception excep) {
                    Log.e(TAG, excep.getMessage());
                    subscriber.onError(excep);
                }
            }
        });
    }

    public Observable<String[]> deliveryOptions() {
        return Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/delivery").build();
                String response = restTemplate.getForObject(components.toUri(), String.class);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> deliveryOptions = new ArrayList<>();
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        JSONObject object = jsonArray.getJSONObject(idx);
                        deliveryOptions.add(object.getString("name"));
                    }
                    String[] cs = new String[deliveryOptions.size()];
                    cs = deliveryOptions.toArray(cs);
                    subscriber.onNext(cs);
                    subscriber.onCompleted();
                } catch (Exception excep) {
                    Log.e(TAG, excep.getMessage(), excep);
                    subscriber.onError(excep);
                }
            }
        });

    }

    public Observable<String[]> specialNeeds() {
        return Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/specialneed").build();
                String response = restTemplate.getForObject(components.toUri(), String.class);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> splNeedsOptions = new ArrayList<>();
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        JSONObject object = jsonArray.getJSONObject(idx);
                        splNeedsOptions.add(object.getString("name"));
                    }
                    String[] cs = new String[splNeedsOptions.size()];
                    cs = splNeedsOptions.toArray(cs);
                    subscriber.onNext(cs);
                    subscriber.onCompleted();
                } catch (Exception excep) {
                    Log.e(TAG, excep.getMessage());
                    subscriber.onError(excep);
                }
            }

        });


    }

    public Observable<String> addToListing(final DinnerMenuItem dinnerMenuItem) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/listing/add").build();
                try {
                    StringBuffer startDate = new StringBuffer();
                    StringBuffer endDate = new StringBuffer();
                    StringBuffer closeDate = new StringBuffer();
                    if (StringUtils.hasText(dinnerMenuItem.getAvailableDate())) {
                        startDate.append(dinnerMenuItem.getAvailableDate());
                        endDate.append(dinnerMenuItem.getAvailableDate());
                        closeDate.append(dinnerMenuItem.getAvailableDate());
                        if (StringUtils.hasText(dinnerMenuItem.getFromTime())) {
                            startDate.append(" " + dinnerMenuItem.getFromTime()).append(":00");
                        }
                        if (StringUtils.hasText(dinnerMenuItem.getCloseTime())) {
                            closeDate.append(" " + dinnerMenuItem.getCloseTime()).append(":00");
                        }
                        if (StringUtils.hasText(dinnerMenuItem.getToTime())) {
                            endDate.append(" " + dinnerMenuItem.getToTime()).append(":00");
                        }
                    }
                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
                    JsonObject requestBody = new JsonObject();
                    requestBody.addProperty("startTime", dateTimeFormatter.print(dateTimeFormatter.parseDateTime(startDate.toString()).toDateTime(DateTimeZone.forID("UTC"))));
                    requestBody.addProperty("endTime", dateTimeFormatter.print(dateTimeFormatter.parseDateTime(endDate.toString()).toDateTime(DateTimeZone.forID("UTC"))));
                    requestBody.addProperty("closeTime", dateTimeFormatter.print(dateTimeFormatter.parseDateTime(closeDate.toString()).toDateTime(DateTimeZone.forID("UTC"))));
                    requestBody.addProperty("availableQuantity", dinnerMenuItem.getAvailableQuantity());
                    requestBody.addProperty("menuItemId", dinnerMenuItem.getId());
                    requestBody.addProperty("costPerItem", dinnerMenuItem.getCost());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
                    String response = restTemplate.postForObject(components.toUri(), entity, String.class);
                    Log.d(TAG, "Received response .....");
                    Log.d(TAG, response);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
                    if (jsonObject.has("code")) {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    subscriber.onNext("Listing saved successfully");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    subscriber.onError(new Exception("Unknown exception occurred while add menu item."));
                }
            }
        });
    }

    public Observable<List<SellerListing>> listingsByProfile(final Integer profileId, final long date) {
        return Observable.create(new Observable.OnSubscribe<List<SellerListing>>() {
            @Override
            public void call(Subscriber<? super List<SellerListing>> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                Map<String, String> variables = new HashMap<>();
                variables.put("profileId", String.valueOf(profileId));

                UriComponents components = uriComponentsBuilder.path("/listing/view/{profileId}")
                        .queryParam("inputDate", date)
                        .buildAndExpand(variables);
                try {
                    String sellerListing = restTemplate.getForObject(components.toUri(), String.class);
                    Gson gson = new Gson();
                    JsonParser jsonParser = new JsonParser();
                    if (jsonParser.parse(sellerListing) instanceof JsonArray) {
                        Type listType = new TypeToken<ArrayList<SellerListing>>() {

                        }.getType();
                        List<SellerListing> listing = gson.fromJson(sellerListing, listType);
                        subscriber.onNext(listing);
                        subscriber.onCompleted();
                    } else {
                        JsonObject jsonObject = (JsonObject) jsonParser.parse(sellerListing);
                        if (jsonObject.has("code")) {
                            ErrorResponse errorResponse = gson.fromJson(sellerListing, ErrorResponse.class);
                            subscriber.onError(new Exception(errorResponse.getMessage()));
                        }
                    }
                } catch (Exception e) {
                    subscriber.onError(new Exception("Unknown exception occurred while add menu item."));
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}
