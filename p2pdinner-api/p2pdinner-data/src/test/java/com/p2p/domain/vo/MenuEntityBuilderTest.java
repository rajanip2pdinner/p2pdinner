package com.p2p.domain.vo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.p2p.data.config.P2PTestContextConfiguration;
import com.p2p.data.service.UserProfileDataService;
import com.p2p.domain.MenuItem;
import com.p2p.domain.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })

public class MenuEntityBuilderTest {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");

	@Autowired
	private EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder;
	
	@Autowired
	private UserProfileDataService userProfileDataService;
	
	private UserProfile testProfile;
	
	@Before
	public void setUp() throws Exception {
		testProfile = new UserProfile();
		testProfile.setEmailAddress("test@eclipse.com");
		testProfile.setPassword("test");
		testProfile.setAddressLine1("5338 Piazza Ct");
		testProfile.setCity("Pleasanton");
		testProfile.setState("California");
		ObjectMapper mapper = new ObjectMapper();
		Writer writer = new StringWriter();
		mapper.writeValue(writer, testProfile);
		testProfile = userProfileDataService.saveProfile(writer.toString());
	}
	
	@Test
	@Transactional
	@Ignore
	public void testBuild() throws Exception {
		MenuItemVO dinnerListingVO = new MenuItemVO();
		dinnerListingVO.setTitle("Fried Rice");
		dinnerListingVO.setDescription("Fried Rice");
		dinnerListingVO.setDinnerCategories("Asian");
		dinnerListingVO.setDinnerDelivery("Dine-In");
		dinnerListingVO.setUserId(testProfile.getId());
		MenuItem listing = menuEntityBuilder.build(dinnerListingVO);
		assertThat(listing, notNullValue());
	}

}
