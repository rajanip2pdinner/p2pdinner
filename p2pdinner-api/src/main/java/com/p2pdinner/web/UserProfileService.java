package com.p2pdinner.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.filepicker.FilePickerOperations;
import com.p2pdinner.common.filepicker.FilePickerUploadResponse;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.common.utils.P2PDinnerUtils;
import com.p2pdinner.domain.*;
import com.p2pdinner.service.DinnerCartDataService;
import com.p2pdinner.service.TierDataService;
import com.p2pdinner.service.UserProfileDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import org.apache.commons.codec.binary.Base64;

@Service
@EnableResourceServer
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

	@Autowired
	private FilePickerOperations filePickerOperations;

	@Path("/validate")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfile(@QueryParam("emailAddress") String emailAddress, @QueryParam("password") String password) throws P2PDinnerException {
		LOGGER.info("Fetching profile with email address : {}", emailAddress);
		Response response = Response.ok().build();
		try {
			if (!StringUtils.hasText(emailAddress)) {
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
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfile(@PathParam("id")Integer id) {
		UserProfile userProfile = userProfileDataService.getProfileById(id);
		return Response.ok(userProfile).build();
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
	public Response saveProfile(@Context UriInfo requestContext, String request) throws P2PDinnerException {
		LOGGER.info("Saving profile...");
		Map<String, Object> responseEntity = new HashMap<String, Object>();
		Response response = Response.ok().build();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(request);
			if (!rootNode.has("emailAddress")) {
				throw excepBuilder.createException(Status.BAD_REQUEST.getStatusCode(), ErrorCode.REQUIRED_FIELD_MISSING, new Object[] {"emailAddress"}, Locale.US);
			}
			if (!rootNode.has("password")) {
				throw excepBuilder.createException(Status.BAD_REQUEST.getStatusCode(), ErrorCode.REQUIRED_FIELD_MISSING, new Object[] {"password"}, Locale.US);
			}
			UserProfile userProfile = userProfileDataService.saveProfile(request);
			responseEntity.put("status", "SUCCESS");
			responseEntity.put("message", "Registration Successful");
			UriBuilder uriBuilder = requestContext.getAbsolutePathBuilder();
			URI uri = uriBuilder.path(UserProfileService.class.getMethod("getProfile", Integer.class)).build(userProfile.getId());
			response = Response.status(Status.CREATED).entity(responseEntity).header("location", uri.toString()).build();
		} catch (P2PDinnerException p2pDinnerException) {
			LOGGER.error(p2pDinnerException.getMessage(), p2pDinnerException);
			throw p2pDinnerException;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(Status.BAD_REQUEST.getStatusCode(), ErrorCode.UNKNOWN, new Object[] { e.getMessage() }, Locale.US);
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
				profile.setStripeCustomerId(rootNode.get("stripe_user_id").asText());
			}
			if (rootNode.has("tier")) {
				Tier tier = tierDataService.tierByName(rootNode.get("tier").asText());
				profile.setTier(tier);
			}
			if (rootNode.has("token_type")) {
				profile.setTokenType(rootNode.get("token_type").asText());
			}
			if (rootNode.has("stripe_publishable_key")) {
				profile.setStripePublishableKey(rootNode.get("stripe_publishable_key").asText());
			}
			if (rootNode.has("livemode")) {
				profile.setLiveMode(rootNode.get("livemode").asBoolean());
			}
			if (rootNode.has("refresh_token")) {
				profile.setRefreshToken(rootNode.get("refresh_token").asText());
			}
			if (rootNode.has("access_token")) {
				profile.setAccessToken(rootNode.get("access_token").asText());
			}
			if (rootNode.has("certificates")) {
				profile.setCertificates(rootNode.get("certificates").asText());
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
		response.put("status", Status.OK);
		response.put("data", cartsList);
		return Response.ok(response).build();	
	}

	@Path("/uploadImage")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
	public Response uploadImage(@FormParam("filename")String filename, @FormParam("image")String image) throws P2PDinnerException{
		String uri = org.apache.commons.lang3.StringUtils.EMPTY;
		try {
			if (!org.apache.commons.lang3.StringUtils.isEmpty(filename)) {
				LOGGER.info(filename);
				if (P2PDinnerUtils.isValidImageExtn(filename)) {
					String fileExtn = P2PDinnerUtils.fileExtn(filename);
					java.nio.file.Path path = Files.createTempFile("img" + System.currentTimeMillis(), "." + fileExtn);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(image));
					Files.copy(byteArrayInputStream, path, StandardCopyOption.REPLACE_EXISTING);
					LOGGER.info("Temp File {}", path);
					// Loading into s3
					FilePickerUploadResponse response = filePickerOperations.upload(path.toString());
					uri = response.getUrl();
				} else {
					throw excepBuilder.createException(ErrorCode.INVALID_FILE_EXTN, new Object[] {P2PDinnerUtils.fileExtn(filename)}, Locale.US);
				}
			}
		} catch (P2PDinnerException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{ e.getMessage() }, Locale.US);
		}
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("url", uri);
		responseMap.put("status", "OK");
		return Response.ok(responseMap).build();
	}
}
