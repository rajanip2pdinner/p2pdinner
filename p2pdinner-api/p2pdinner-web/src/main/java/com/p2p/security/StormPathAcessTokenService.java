package com.p2p.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.oauth2.services.AbstractTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.oauth.Authenticators;
import com.stormpath.sdk.oauth.Oauth2Requests;
import com.stormpath.sdk.oauth.OauthGrantAuthenticationResult;
import com.stormpath.sdk.oauth.PasswordGrantRequest;
import com.stormpath.sdk.oauth.RefreshGrantRequest;

@Path("/token")
public class StormPathAcessTokenService extends AbstractTokenService {

	private static final Logger _logger = LoggerFactory.getLogger(StormPathAcessTokenService.class);

	@Autowired
	private com.stormpath.sdk.client.Client stormPathClient;

	private static final String APPLICATION_HREF = "https://api.stormpath.com/v1/applications/2e2VTO48vmSJO8MJtBmoXT";

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response handleTokenRequest(@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse, MultivaluedMap<String, String> params) {
		_logger.info("Requesting authentication token ...");
		Application application = stormPathClient.getResource(APPLICATION_HREF, Application.class);

		String grantType = params.getFirst("grant_type");
		if (grantType.equalsIgnoreCase("password")) {
			PasswordGrantRequest passwordGrantRequest = Oauth2Requests.PASSWORD_GRANT_REQUEST.builder()
					.setLogin(params.getFirst("username")).setPassword(params.getFirst("password")).build();
			OauthGrantAuthenticationResult oauthGrantAuthenticationResult = Authenticators.PASSWORD_GRANT_AUTHENTICATOR
					.forApplication(application).authenticate(passwordGrantRequest);
			Map<String, Object> responseEntity = new HashMap<>();
			responseEntity.put("access_token", oauthGrantAuthenticationResult.getAccessTokenString());
			responseEntity.put("refresh_token", oauthGrantAuthenticationResult.getRefreshTokenString());
			responseEntity.put("token_type", oauthGrantAuthenticationResult.getTokenType());
			responseEntity.put("expires_in", oauthGrantAuthenticationResult.getExpiresIn());
			return Response.ok(responseEntity).build();
		} else if (grantType.equalsIgnoreCase("refresh_token")) {
			RefreshGrantRequest refreshGrantRequest = Oauth2Requests.REFRESH_GRANT_REQUEST.builder()
					.setRefreshToken(params.getFirst("refresh_token"))
					.build();
			OauthGrantAuthenticationResult oauthGrantAuthenticationResult = Authenticators.REFRESH_GRANT_AUTHENTICATOR
					.forApplication(application).authenticate(refreshGrantRequest);
			Map<String, Object> responseEntity = new HashMap<>();
			responseEntity.put("access_token", oauthGrantAuthenticationResult.getAccessTokenString());
			responseEntity.put("refresh_token", oauthGrantAuthenticationResult.getRefreshTokenString());
			responseEntity.put("token_type", oauthGrantAuthenticationResult.getTokenType());
			responseEntity.put("expires_in", oauthGrantAuthenticationResult.getExpiresIn());
			return Response.ok(responseEntity).build();
		}
		throw new BadRequestException("Invalid grant type");

	}
}
