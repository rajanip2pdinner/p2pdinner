package com.p2pdinner.restclient;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;
import com.p2pdinner.entities.DinnerListing;
import com.p2pdinner.entities.Order;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by rajaniy on 12/30/15.
 */
public class DinnerCartManager {
    private static final String TAG = DinnerCartManager.class.getName();

    private RestTemplate restTemplate;

    public DinnerCartManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public Observable<Integer> addToCart(final Long buyerProfileId, final Integer listingId, final Integer quantity, final String deliveryType) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/cart").build();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("listing_id", listingId);
                jsonObject.addProperty("profile_id", buyerProfileId);
                jsonObject.addProperty("quantity", quantity);
                jsonObject.addProperty("delivery_type", deliveryType);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
                try {
                    String response = restTemplate.postForObject(components.toUri(), entity, String.class);
                    JsonElement jsonElement = new JsonParser().parse(response);
                    JsonObject responseJson = jsonElement.getAsJsonObject();
                    if (responseJson.has("code")) {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    Integer cartId = responseJson.get("response").getAsJsonObject().get("cartId").getAsInt();
                    subscriber.onNext(cartId);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<String> placeOrder(final Long profileId, final Integer cartId) {
        return Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                Map<String,Object> variables = new HashMap<>();
                variables.put("profileId", profileId);
                variables.put("cartId", cartId);
                UriComponents components = uriComponentsBuilder.path("/cart/placeorder/{profileId}/{cartId}").buildAndExpand(variables);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(null, headers);
                try {
                    String response = restTemplate.postForObject(components.toUri(), entity, String.class);
                    JsonElement jsonElement = new JsonParser().parse(response);
                    JsonObject responseJson = jsonElement.getAsJsonObject();
                    if (responseJson.has("code")) {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    String msg = responseJson.get("response").getAsJsonObject().get("message").getAsString();
                    subscriber.onNext(msg);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Order>> receivedOrders(final Integer listingId) {
        return Observable.create(new Observable.OnSubscribe<List<Order>>() {
            @Override
            public void call(Subscriber<? super List<Order>> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                Map<String,Object> variables = new HashMap<>();
                variables.put("listingId", listingId);
                UriComponents components = uriComponentsBuilder.path("/cart/orders/{listingId}/received/detail").buildAndExpand(variables);
                try {
                    String response = restTemplate.getForObject(components.toUri(), String.class);
                    JsonElement jsonElement = new JsonParser().parse(response);
                    JsonObject responseJson = jsonElement.getAsJsonObject();
                    if (responseJson.has("code")) {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    JsonArray results = responseJson.get("results").getAsJsonArray();
                    Gson gson = new GsonBuilder().create();
                    List<Order> orders = gson.fromJson(results, new TypeToken<List<Order>>(){}.getType());
                    subscriber.onNext(orders);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Order>> receivedOrdersByBuyer(final Long buyerProfileId, final DateTime startTime, final DateTime endTime) {
        return Observable.create(new Observable.OnSubscribe<List<Order>>() {
            @Override
            public void call(Subscriber<? super List<Order>> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                Map<String,Object> variables = new HashMap<>();
                variables.put("profileId", buyerProfileId);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC();
                uriComponentsBuilder.queryParam("startDate", formatter.print(startTime));
                uriComponentsBuilder.queryParam("endDate", formatter.print(endTime));
                UriComponents components = uriComponentsBuilder.path("/cart/placedorders/{profileId}").buildAndExpand(variables);
                try {
                    String response = restTemplate.getForObject(components.toUri(), String.class);
                    JsonElement jsonElement = new JsonParser().parse(response);
                    JsonObject responseJson = jsonElement.getAsJsonObject();
                    if (responseJson.has("code")) {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    JsonArray results = responseJson.get("results").getAsJsonArray();
                    Gson gson = new GsonBuilder().create();
                    List<Order> orders = gson.fromJson(results, new TypeToken<List<Order>>(){}.getType());
                    subscriber.onNext(orders);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }
}
