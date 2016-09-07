package com.p2pdinner.restclient;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.AppAccessToken;
import com.p2pdinner.services.P2PDinnerOAuthTokenRefreshService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by rajaniy on 6/1/16.
 */

public class P2PDinnerAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private static final String TAG = "P2PDinner";

    @Inject
    P2PDinnerOAuthTokenRefreshService p2PDinnerOAuthTokenRefreshService;

    public P2PDinnerAuthenticationInterceptor(P2PDinnerOAuthTokenRefreshService p2PDinnerOAuthTokenRefreshService) {
        this.p2PDinnerOAuthTokenRefreshService = p2PDinnerOAuthTokenRefreshService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().getPath().contains("oauth")) {
            Log.i(TAG, "OAuth Request - skipping authentication headers");
            return execution.execute(request, body);
        }
        Log.i(TAG, "Setting authentication headers ... ");
        HttpHeaders headers = request.getHeaders();
        AppAccessToken appAccessToken = p2PDinnerOAuthTokenRefreshService.getAccessToken();
        headers.set("Authorization", "Bearer " + appAccessToken.getAccessToken());
        return execution.execute(request, body);
    }


}
