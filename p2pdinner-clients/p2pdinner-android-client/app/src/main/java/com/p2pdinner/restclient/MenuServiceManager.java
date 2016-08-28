package com.p2pdinner.restclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by rajaniy on 10/7/15.
 */
public class MenuServiceManager {
    private static String TAG = MenuServiceManager.class.getName();
    private static MenuServiceManager menuServiceManager;

    private RestTemplate restTemplate;

    public MenuServiceManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Observable<String> uploadBitMap(final String filename, final Bitmap bitmap) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i(TAG, "Uploading image -" + filename);
                List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
                messageConverters.add(new FormHttpMessageConverter());
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/menu/uploadImage").build();
                MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
                parts.add("filename", filename);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                parts.add("image", Base64Utils.encodeToString(byteArrayOutputStream.toByteArray()));
                try {
                    String response = restTemplate.postForObject(components.toUri(), parts, String.class);
                    Log.d(TAG, response);
                    JsonElement jsonElement = new JsonParser().parse(response);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String url = jsonObject.get("url").toString();
                    subscriber.onNext(url);
                    subscriber.onCompleted();
                } catch (Throwable t) {
                    subscriber.onError(t);
                }
            }
        });
    }




    public Observable<DinnerMenuItem> saveMenuItem(final DinnerMenuItem dinnerMenuItem) {
        return Observable.create(new Observable.OnSubscribe<DinnerMenuItem>() {
            @Override
            public void call(Subscriber<? super DinnerMenuItem> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/menu/add").build();
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (!StringUtils.hasText(dinnerMenuItem.getTitle())) {
                        return;
                    }
                    jsonObject.put("title", dinnerMenuItem.getTitle());
                    if (StringUtils.hasText(dinnerMenuItem.getDescription())) {
                        jsonObject.put("description", dinnerMenuItem.getDescription());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getCategories())) {
                        jsonObject.put("dinnerCategories", dinnerMenuItem.getCategories());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getDeliveryOptions())) {
                        jsonObject.put("dinnerDelivery", dinnerMenuItem.getDeliveryOptions());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getSpecialNeeds())) {
                        jsonObject.put("dinnerSpecialNeeds", dinnerMenuItem.getSpecialNeeds());
                    }
                    jsonObject.put("userId", dinnerMenuItem.getProfileId());
                    jsonObject.put("isActive", Boolean.TRUE);
                    if (dinnerMenuItem.getId() != null) {
                        jsonObject.put("id", dinnerMenuItem.getId());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getAddressLine1())) {
                        jsonObject.put("addressLine1", dinnerMenuItem.getAddressLine1());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getAddressLine2())) {
                        jsonObject.put("addressLine2", dinnerMenuItem.getAddressLine2());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getCity())) {
                        jsonObject.put("city", dinnerMenuItem.getCity());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getState())) {
                        jsonObject.put("state", dinnerMenuItem.getState());
                    }
                    if (StringUtils.hasText(dinnerMenuItem.getImageUri())) {
                        jsonObject.put("imageUri", dinnerMenuItem.getImageUri());
                    }
                    if (dinnerMenuItem.getAvailableQuantity() != null) {
                        jsonObject.put("availableQuantity", dinnerMenuItem.getAvailableQuantity());
                    }
                    if (dinnerMenuItem.getCost() != null) {
                        jsonObject.put("costPerItem", dinnerMenuItem.getCost());
                    }
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
                        jsonObject.put("startDate", startDate.toString());
                        jsonObject.put("endDate", endDate.toString());
                        jsonObject.put("closeDate", endDate.toString());
                    }
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
                    DinnerMenuItem savedItem = restTemplate.postForObject(components.toUri(), entity, DinnerMenuItem.class);
                    Log.d(TAG, "Received response .....");
                    subscriber.onNext(savedItem);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<DinnerMenuItem>> favouritesByProfileId(final Long profileId) {
        Observable<List<DinnerMenuItem>> dinnerMenuItemObservable = Observable.create(new Observable.OnSubscribe<List<DinnerMenuItem>>() {
            @Override
            public void call(Subscriber<? super List<DinnerMenuItem>> subscriber) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                builder.path("/menu/view/" + profileId);
                UriComponents uriComponents = builder.build();
                List<DinnerMenuItem> dinnerMenuItemList;
                try {
                    ResponseEntity<DinnerMenuItem[]> responseEntity = restTemplate.getForEntity(uriComponents.toUri(), DinnerMenuItem[].class);
                    dinnerMenuItemList = (List<DinnerMenuItem>) CollectionUtils.arrayToList(responseEntity.getBody());
                    subscriber.onNext(dinnerMenuItemList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
        return dinnerMenuItemObservable;
    }

    public Observable<DinnerMenuItem> menuItemById(final Long menuItemId) {
        return Observable.create(new Observable.OnSubscribe<DinnerMenuItem>() {
            @Override
            public void call(Subscriber<? super DinnerMenuItem> subscriber) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                builder.path("/menu/" + menuItemId);
                UriComponents uriComponents = builder.build();
                try {
                    DinnerMenuItem dinnerMenuItem = restTemplate.getForObject(uriComponents.toUri(), DinnerMenuItem.class);
                    subscriber.onNext(dinnerMenuItem);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
