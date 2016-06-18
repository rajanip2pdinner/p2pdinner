package com.p2p.security;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.AbstractGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.oauth.Authenticators;
import com.stormpath.sdk.oauth.Oauth2Requests;
import com.stormpath.sdk.oauth.OauthGrantAuthenticationResult;
import com.stormpath.sdk.oauth.PasswordGrantRequest;

public class StormPathAccessTokenGrantHandler extends AbstractGrantHandler {

	private static final Logger _logger = LoggerFactory.getLogger(StormPathAccessTokenGrantHandler.class);

	private static final String STORMPATH_TOKEN_REQUEST_URL = "https://api.stormpath.com/v1/applications/2e2VTO48vmSJO8MJtBmoXT/oauth/token";
	private static final String APPLICATION_HREF = "https://api.stormpath.com/v1/applications/2e2VTO48vmSJO8MJtBmoXT";

	@Autowired
	private com.stormpath.sdk.client.Client stormPathClient;

	protected StormPathAccessTokenGrantHandler(List<String> grants) {
		super(grants);
	}

	@Override
	public ServerAccessToken createAccessToken(Client client, MultivaluedMap<String, String> params)
			throws OAuthServiceException {
		Application application = stormPathClient.getResource(APPLICATION_HREF, Application.class);
		PasswordGrantRequest passwordGrantRequest = Oauth2Requests.PASSWORD_GRANT_REQUEST.builder()
				  .setLogin("username@test.com")
				  .setPassword("Password1!")
				  .build();
		OauthGrantAuthenticationResult oauthGrantAuthenticationResult = Authenticators.PASSWORD_GRANT_AUTHENTICATOR
				  .forApplication(application)
				  .authenticate(passwordGrantRequest);
		return null;
	}

}
