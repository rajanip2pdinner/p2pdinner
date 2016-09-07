package com.p2pdinner.restclient;

import android.content.Context;
import android.util.Log;

import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;
import com.p2pdinner.entities.AppAccessToken;
import com.p2pdinner.entities.UserProfile;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by rajaniy on 7/21/15.
 */
public class UserProfileManager {

    private static String TAG = UserProfileManager.class.getName();

    private RestTemplate restTemplate;

    private Context context;

    public UserProfileManager(RestTemplate restTemplate, Context context) {
        this.restTemplate = restTemplate;
        this.context = context;
    }

    public Observable<UserProfile> validateProfile(final String emailAddress, final String password) {
        return Observable.create(new Observable.OnSubscribe<UserProfile>() {
            @Override
            public void call(Subscriber<? super UserProfile> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/profile/validate").queryParam("emailAddress", emailAddress).queryParam("password", password).build();
                String response = restTemplate.getForObject(components.toUri(), String.class);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        Log.e(TAG, "Received error response ----" + response);
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.setCode(jsonObject.getString("code"));
                        errorResponse.setMessage(jsonObject.getString("message"));
                        errorResponse.setStatus(jsonObject.getInt("status"));
                        subscriber.onError(new Exception(errorResponse.getMessage()));
                    }
                    UserProfile userProfile = buildUserProfile(jsonObject);
                    subscriber.onNext(userProfile);
                    subscriber.onCompleted();
                } catch (Exception excep) {
                    subscriber.onError(excep);
                }
            }
        });

    }

    private static UserProfile buildUserProfile(JSONObject jsonObject) throws JSONException {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmailAddress(jsonObject.getString("emailAddress"));
        userProfile.setId(jsonObject.getInt("id"));
        if (hasValidData(jsonObject, "addressLine1")) {
            userProfile.setAddressLine1(jsonObject.getString("addressLine1"));
        }
        if (hasValidData(jsonObject, "addressLine2")) {
            userProfile.setAddressLine1(jsonObject.getString("addressLine2"));
        }
        if (hasValidData(jsonObject, "city")) {
            userProfile.setCity(jsonObject.getString("city"));
        }
        if (hasValidData(jsonObject, "state")) {
            userProfile.setState(jsonObject.getString("state"));
        }
        if (hasValidData(jsonObject, "zip")) {
            userProfile.setZip(jsonObject.getString("zip"));
        }
        if (hasValidData(jsonObject, "latitude")) {
            userProfile.setLatitude(jsonObject.getDouble("latitude"));
        }
        if (hasValidData(jsonObject, "longitude")) {
            userProfile.setLongitude(jsonObject.getDouble("longitude"));
        }
        return userProfile;
    }

    private static boolean hasValidData(JSONObject jsonObject, String jsonField) throws JSONException {
        return jsonObject.has(jsonField) && jsonObject.getString(jsonField) != null && !jsonObject.getString(jsonField).equalsIgnoreCase("null");
    }

    public Observable<Void> createProfile(final UserProfile userProfile) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
                UriComponents components = uriComponentsBuilder.path("/profile").build();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("emailAddress", userProfile.getEmailAddress());
                    jsonObject.put("password", userProfile.getPassword());
                    jsonObject.put("profile_name", userProfile.getName());
                    jsonObject.put("authentication_provider", userProfile.getAuthenticationProvider());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
                    String response = restTemplate.postForObject(components.toUri(), entity, String.class);
                    Log.i(TAG, "Received response for create profile ----- " + response);
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (Exception excep) {
                    subscriber.onError(excep);
                }
            }
        });
    }


}
