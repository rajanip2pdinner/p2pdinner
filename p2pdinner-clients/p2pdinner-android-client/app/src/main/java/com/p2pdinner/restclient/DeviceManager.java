package com.p2pdinner.restclient;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajaniy on 1/5/16.
 */
public class DeviceManager {

    private static DeviceManager deviceManager;

    private DeviceManager(){

    }

    public static DeviceManager getInstance() {
        if (deviceManager == null) {
            deviceManager = new DeviceManager();
        }
        return deviceManager;
    }

    public boolean addDevice(Long profileId, String deviceToken) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
        Map<String,Object> variables = new HashMap<>();
        variables.put("id", profileId);
        UriComponents components = uriComponentsBuilder.path("/profile/{id}/devices").buildAndExpand(variables);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceType", "Android");
        jsonObject.addProperty("registrationId", deviceToken);
        jsonObject.addProperty("notificationsEnabled", true);
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
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
