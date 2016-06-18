package com.p2p.data.service;

import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.p2p.data.config.P2PTestContextConfiguration;
import com.p2p.domain.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class UserProfileDataServiceTest {

	@Autowired
	private UserProfileDataService userProfileService;

	private UserProfile testProfile = new UserProfile();

	@Before
	public void setUp() {
		testProfile = new UserProfile();
		testProfile.setEmailAddress("test@eclipse.com");
		testProfile.setPassword("test");
		testProfile.setAddressLine1("5338");
		testProfile.setAddressLine2("Piazza Ct");
		testProfile.setCity("Pleasanton");
		testProfile.setState("California");
	}

	@Test
	@Rollback
	@Ignore
	public void testSaveProfile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
			mapper.setSerializationInclusion(Inclusion.NON_NULL);
			String request = mapper.writeValueAsString(testProfile);
			UserProfile savedProfile = userProfileService.saveProfile(request);
			Assert.assertNotNull(savedProfile);
			Assert.assertNotNull(savedProfile.getId());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	@Rollback
	@Ignore
	public void testUpdateProfile() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Writer writer = new StringWriter();
		mapper.writeValue(writer, testProfile);
		UserProfile saveProfile = userProfileService.saveProfile(writer.toString());
		writer = new StringWriter();
		Integer beforeUpdate = saveProfile.getId();
		testProfile.setPassword("abc");
		testProfile.setId(null);
		mapper.writeValue(writer, testProfile);
		saveProfile = userProfileService.saveProfile(writer.toString());
		Integer afterUpdate = saveProfile.getId();
		Assert.assertEquals(beforeUpdate, afterUpdate);

	}
}
