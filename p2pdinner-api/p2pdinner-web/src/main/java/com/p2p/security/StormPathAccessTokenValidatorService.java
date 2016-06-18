package com.p2p.security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenValidation;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.services.AbstractAccessTokenValidator;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.oauth.AccessToken;
import com.stormpath.sdk.oauth.Authenticators;
import com.stormpath.sdk.oauth.JwtAuthenticationRequest;
import com.stormpath.sdk.oauth.JwtAuthenticationResult;
import com.stormpath.sdk.oauth.Oauth2Requests;

public class StormPathAccessTokenValidatorService extends AbstractAccessTokenValidator {
	
	@Autowired
    private com.stormpath.sdk.client.Client client;
    private static final String APPLICATION_HREF = "https://api.stormpath.com/v1/applications/2e2VTO48vmSJO8MJtBmoXT";
	
	@Path("validate")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public AccessTokenValidation getTokenValidationInfo(@Context HttpHeaders httpHeaders) {
		Application application = client.getResource(APPLICATION_HREF, Application.class);
        JwtAuthenticationRequest jwtRequest = Oauth2Requests.JWT_AUTHENTICATION_REQUEST.builder()
                .setJwt(httpHeaders.getHeaderString("Authorization"))
                .build();
        JwtAuthenticationResult jwtAuthenticationResult = Authenticators.JWT_AUTHENTICATOR
                .forApplication(application)
                .authenticate(jwtRequest);
        AccessToken accessToken = client.getDataStore().getResource(jwtAuthenticationResult.getHref(), AccessToken.class);
        ServerAccessToken serverAccessToken = new BearerAccessToken();
        serverAccessToken.setClient(new Client());
        //serverAccessToken.setExpiresIn(Long.valueOf(application.getOauthPolicy().getAccessTokenTtl()));
        return new AccessTokenValidation(serverAccessToken);
	}
}
