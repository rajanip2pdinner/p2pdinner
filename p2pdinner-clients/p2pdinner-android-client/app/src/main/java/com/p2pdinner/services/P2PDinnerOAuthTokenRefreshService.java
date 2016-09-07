package com.p2pdinner.services;

import android.content.Context;
import android.util.Log;

import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.AppAccessToken;
import com.p2pdinner.restclient.UserProfileManager;

import org.apache.commons.codec.binary.Base64;
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

import javax.inject.Inject;

public class P2PDinnerOAuthTokenRefreshService {

    private static final String TAG = "P2PDinnerOAuth";

    private AppAccessToken appAccessToken;
    private Context context;
    private RestTemplate oauthRequestTemplate;

    public P2PDinnerOAuthTokenRefreshService(Context context, RestTemplate oauthRequestTemplate) {
        this.context = context;
        this.oauthRequestTemplate = oauthRequestTemplate;
    }


    public AppAccessToken getAccessToken() {
        if (appAccessToken == null) {
            Log.d(TAG, "Request new token");
            appAccessToken = requestAccessToken();
        } else  {
            if (appAccessToken.isExpired()) {
                appAccessToken = refreshAccessToken(appAccessToken.getRefreshToken());
            }
        }
        return appAccessToken;
    }

    public AppAccessToken requestAccessToken() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
        UriComponents components = uriComponentsBuilder.path("/oauth/token").build();
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
        formParams.add("grant_type", "Password");
        formParams.add("username", context.getString(R.string.p2pdinner_user_name));
        formParams.add("password", context.getString(R.string.p2pdinner_password));
        HttpHeaders httpHeaders = createHeaders(context.getString(R.string.p2pdinner_client_id), context.getString(R.string.p2pdinner_client_secret));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, httpHeaders);
        ResponseEntity<AppAccessToken> appAccessTokenEntity = oauthRequestTemplate.exchange(components.toUri(), HttpMethod.POST, requestEntity, AppAccessToken.class);
        return appAccessTokenEntity.getBody();
    }

    public AppAccessToken refreshAccessToken(final String refreshToken) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(Constants.P2PDINNER_BASE_URI);
        UriComponents components = uriComponentsBuilder.path("/oauth/token").build();
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
        formParams.add("grant_type", "refresh_token");
        formParams.add("refresh_token", refreshToken);
        HttpHeaders httpHeaders = createHeaders(context.getString(R.string.p2pdinner_client_id), context.getString(R.string.p2pdinner_client_secret));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, httpHeaders);
        ResponseEntity<AppAccessToken> appAccessTokenEntity = oauthRequestTemplate.exchange(components.toUri(), HttpMethod.POST, requestEntity, AppAccessToken.class);
        return appAccessTokenEntity.getBody();
    }


    private HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

}
