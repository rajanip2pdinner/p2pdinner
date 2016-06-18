package com.p2p.security;

import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.oauth.*;

import java.util.Arrays;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.provider.AbstractOAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rajaniy on 5/1/16.
 */
public class StormPathDataProvider extends AbstractOAuthDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(StormPathDataProvider.class);


    @Autowired
    private com.stormpath.sdk.client.Client client;




    private static final String APPLICATION_HREF = "https://api.stormpath.com/v1/applications/2e2VTO48vmSJO8MJtBmoXT";

    @Override
    protected void saveAccessToken(ServerAccessToken serverAccessToken) {
        LOGGER.info("saveAccessToken");
    }

    @Override
    protected void saveRefreshToken(ServerAccessToken serverAccessToken, RefreshToken refreshToken) {
        LOGGER.info("saveRefreshToken");
    }

    @Override
    protected boolean revokeAccessToken(String s) {
        LOGGER.info("revokeAccessToken");
        return false;
    }

    @Override
    protected RefreshToken revokeRefreshToken(Client client, String s) {
        LOGGER.info("revokeRefreshToken");
        return null;
    }

    @Override
    public Client getClient(String s) throws OAuthServiceException {
        LOGGER.info("getClient");
        Client webClient = new Client();
        webClient.setClientId(client.getApiKey().getId());
        webClient.setClientSecret(client.getApiKey().getSecret());
        webClient.setConfidential(true);
        webClient.setAllowedGrantTypes(Arrays.asList("password"));
        return webClient;
    }

    @Override
    public ServerAccessToken getAccessToken(String s) throws OAuthServiceException {
        Application application = client.getResource(APPLICATION_HREF, Application.class);
        JwtAuthenticationRequest jwtRequest = Oauth2Requests.JWT_AUTHENTICATION_REQUEST.builder()
                .setJwt(s)
                .build();
        JwtAuthenticationResult jwtAuthenticationResult = Authenticators.JWT_AUTHENTICATOR
                .forApplication(application)
                .authenticate(jwtRequest);
        AccessToken accessToken = client.getDataStore().getResource(jwtAuthenticationResult.getHref(), AccessToken.class);
        ServerAccessToken serverAccessToken = new BearerAccessToken();
        serverAccessToken.setClient(new Client());
        //serverAccessToken.setExpiresIn(Long.valueOf(application.getOauthPolicy().getAccessTokenTtl()));
        return serverAccessToken;
    }
}
