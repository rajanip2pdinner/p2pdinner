package com.p2p.services;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.data.service.DinnerCartDataService;
import com.p2p.data.service.TierDataService;
import com.p2p.data.service.UserProfileDataService;
import com.p2p.domain.*;
import org.apache.cxf.common.util.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Path("/profile")
public class UserProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

	@Autowired
	private UserProfileDataService userProfileDataService;

	@Autowired
	private TierDataService tierDataService;
	
	@Autowired
	private DinnerCartDataService dinnerCartDataService;

	@Autowired
	private ExceptionMessageBuilder excepBuilder;

	@Path("/validate")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfile(@QueryParam("emailAddress") String emailAddress, @QueryParam("password") String password) throws P2PDinnerException {
		LOGGER.info("Fetching profile with email address : {}", emailAddress);
		Response response = Response.ok().build();
		try {
			if (StringUtils.isEmpty(emailAddress)) {
				response = Response.status(Status.BAD_REQUEST).build();
			} else {
				UserProfile userProfile = userProfileDataService.findUserProfileByEmailAddressAndPassword(emailAddress, password);
				if (userProfile == null) {
					throw excepBuilder.createException(ErrorCode.UNAUTHORIZED, new Object[] { emailAddress }, Locale.US);
				}
				response = Response.ok(userProfile).build();
			}
		} catch (P2PDinnerException appException) {
			LOGGER.error(appException.getMessage(), appException);
			throw appException;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage() }, Locale.US);
		}
		return response;
	}

	@Path("/states")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response states() {
		Collection<State> states = userProfileDataService.getAllStates();
		return Response.ok(states).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveProfile(String request) throws P2PDinnerException {
		LOGGER.info("Saving profile...");
		Map<String, Object> responseEntity = new HashMap<String, Object>();
		Response response = Response.ok().build();
		try {
			UserProfile userProfile = userProfileDataService.saveProfile(request);
			response = Response.ok(userProfile).build();
			responseEntity.put("status", "SUCCESS");
			responseEntity.put("message", "Registration Successful");
			response = Response.ok(responseEntity).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage() }, Locale.US);
		}
		return response;
	}

	@Path("/{id}/devices")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDevice(@PathParam("id")Integer profileId,String requestBody) throws P2PDinnerException {
		Response response = Response.ok().build();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Device device = mapper.readValue(requestBody, Device.class);
			userProfileDataService.addDevice(profileId, device);
			Map<String,Object> responseMap = new HashMap<>();
			responseMap.put("status", "OK");
			UserProfile profile = userProfileDataService.getProfileById(profileId);
			responseMap.put("devices", profile.getDevices());
			response = Response.ok(responseMap).build();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (P2PDinnerException excep) {
			throw excep;
		}
		return response;
	}

	@PUT
	@Path("/{id}/activate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activateProfile(@PathParam("id") Integer id) {
		LOGGER.info("Activating profile...");
		Response response;
		try {
			UserProfile profile = userProfileDataService.activateProfile(id);
			response = Response.ok(profile).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to active profile. Profile does not exist.").build();
		}
		return response;
	}

	@PUT
	@Path("/{id}/deactivate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deactivateProfile(@PathParam("id") Integer id) {
		LOGGER.info("Activating profile...");
		Response response;
		try {
			UserProfile profile = userProfileDataService.deactivateProfile(id);
			response = Response.ok(profile).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to deactive profile. Profile does not exist.").build();
		}
		return response;
	}

	@PUT
	@Path("/{id}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProfile(@PathParam("id") Integer id, String body) throws P2PDinnerException {
		LOGGER.info("Updating profile....");
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(body);
			UserProfile profile = userProfileDataService.getProfileById(id);
			if (profile == null) {
				throw excepBuilder.createException(ErrorCode.INVALID_PROFILE, new Object[] { id }, Locale.US);
			}
			if (rootNode.has("stripe_user_id")) {
				profile.setStripeCustomerId(rootNode.get("stripe_user_id").getTextValue());
			}
			if (rootNode.has("tier")) {
				Tier tier = tierDataService.tierByName(rootNode.get("tier").getTextValue());
				profile.setTier(tier);
			}
			if (rootNode.has("token_type")) {
				profile.setTokenType(rootNode.get("token_type").getTextValue());
			}
			if (rootNode.has("stripe_publishable_key")) {
				profile.setStripePublishableKey(rootNode.get("stripe_publishable_key").getTextValue());
			}
			if (rootNode.has("livemode")) {
				profile.setLiveMode(rootNode.get("livemode").getBooleanValue());
			}
			if (rootNode.has("refresh_token")) {
				profile.setRefreshToken(rootNode.get("refresh_token").getTextValue());
			}
			if (rootNode.has("access_token")) {
				profile.setAccessToken(rootNode.get("access_token").getTextValue());
			}
			userProfileDataService.saveProfile(profile);
			return Response.ok(profile).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage() }, Locale.US);
		}
	}
	
	@GET
	@Path("/{id}/order-history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderHistory(@PathParam("id")Integer profileId) throws P2PDinnerException {
		Collection<DinnerCart> carts = dinnerCartDataService.orderHistory(profileId);
		Map<String,Object> response = new HashMap<String,Object>();
		List<Map<String,Object>> cartsList = new ArrayList<Map<String,Object>>();
		for(DinnerCart cart : carts) {
			Map<String,Object> dciMap = new HashMap<String,Object>();
			float totalPrice = 0f;
			for(DinnerCartItem dci : cart.getCartItems()) {
				totalPrice += dci.getTotalPrice();
			}
			dciMap.put("id", cart.getId());
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
			dciMap.put("created_date", dateFormat.format(cart.getCreatedDate().getTime()));
			dciMap.put("modified_date", dateFormat.format(cart.getModifiedDate().getTime()));
			dciMap.put("total_price", totalPrice);
			dciMap.put("pass_code", cart.getPassCode());
			cartsList.add(dciMap);
		}
		response.put("status", HttpStatus.OK.name());
		response.put("data", cartsList);
		return Response.ok(response).build();	
	}
}
