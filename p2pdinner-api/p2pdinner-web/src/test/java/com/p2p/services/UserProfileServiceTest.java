package com.p2p.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Locale;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.data.service.UserProfileDataService;
import com.p2p.domain.UserProfile;


public class UserProfileServiceTest {

	@InjectMocks
	private UserProfileService userProfileService = new UserProfileService();
	
	@Mock
	private UserProfileDataService userProfileDataService;
	
	@Mock
	private ExceptionMessageBuilder excepBuilder;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetProfile() throws Exception {
		UserProfile testProfile = new UserProfile();
		testProfile.setEmailAddress("test@eclipse.com");
		testProfile.setPassword("test");
		testProfile.setAddressLine1("12345");
		testProfile.setAddressLine2("Test Blvd");
		testProfile.setCity("Test");
		testProfile.setState("CA");
		when(userProfileDataService.findUserProfileByEmailAddressAndPassword(anyString(), anyString())).thenReturn(testProfile);
		Response response = userProfileService.getProfile("test@eclipse.com", "test");
		assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
	}
	
	@Test
	public void testGetProfileWithNoEmailAddress() throws Exception {
		Response response = userProfileService.getProfile(StringUtils.EMPTY, StringUtils.EMPTY);
		assertThat(response.getStatus(), equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
	}
	
	@Test(expected = P2PDinnerException.class)
	@Ignore
	public void testGetProfileWithException() throws Exception {
		when(userProfileDataService.findUserProfileByEmailAddressAndPassword(anyString(), anyString())).thenThrow(new Exception("Failed to get profile"));
		when(excepBuilder.createException(any(ErrorCode.class), any(Object[].class), any(Locale.class))).thenThrow(new P2PDinnerException());
		Response response = userProfileService.getProfile("test@eclipse.com", "test");
	}
	
	@Test
	public void testSaveProfile() throws Exception {
		UserProfile testProfile = new UserProfile();
		testProfile.setEmailAddress("test@eclipse.com");
		testProfile.setPassword("test");
		testProfile.setAddressLine1("12345");
		testProfile.setAddressLine2("Test Blvd");
		testProfile.setCity("Test");
		testProfile.setState("CA");
		Resource resource = new ClassPathResource("/userProfile.json");
		String request = IOUtils.toString(resource.getInputStream());
		when(userProfileDataService.saveProfile(Mockito.anyString())).thenReturn(testProfile);
		Response response = userProfileService.saveProfile(request);
		assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
	}
}
