package com.p2pdinner.restclient;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;
import com.p2pdinner.common.Constants;

/**
 * Created by rajaniy on 10/18/15.
 */
public class GoogleApiService {
    private static final String TAG = GoogleApiService.class.getName();

    private static GoogleApiService googleApiService;

    public GoogleApiService() {

    }

    public void getAddress(Handler handler, String clientId, String clientKey, Location location) throws Exception {
        try {
            final Geocoder geocoder = new Geocoder();
            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()))).getGeocoderRequest();
            GeocodeResponse geocodeResponse = geocoder.geocode(geocoderRequest);
            Message message = handler.obtainMessage(Constants.Message.GEOCODE_REVERSE_LOOKUP, geocodeResponse);
            message.sendToTarget();
        } catch (Exception e) {
            Log.e(TAG, "Failed to resolve latitude: " + location.getLatitude() + " and longitude: " + location.getLongitude());
            Log.e(TAG, e.getMessage());
        }
    }
}
