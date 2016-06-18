package com.p2p.data.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.p2p.data.repositories.MenuRepository;
import com.p2p.domain.MenuItem;
import com.p2p.domain.UserProfile;
import com.p2p.domain.vo.EntityBuilder;
import com.p2p.domain.vo.MenuItemVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
public class MenuDataServiceTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuDataServiceTest.class);
	
	@Autowired
	private MenuDataService menuDataService;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private UserProfileDataService userProfileDataService;
	
	@Autowired
	private EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder;
	
	private UserProfile testProfile;
	
	@Before
	public void setUp() throws Exception {
		Resource resource = new ClassPathResource("/userProfile.json");
		String request = IOUtils.toString(resource.getInputStream());
		testProfile = userProfileDataService.saveProfile(request);
	}
	
	@Test
	@Rollback
	@Ignore
	public void testSaveListing() throws Exception {
		MenuItemVO menuItem = new MenuItemVO();
		menuItem.setTitle("Veg Fried Rice");
		menuItem.setDescription("Veg Fried Rice");
		menuItem.setDinnerCategories("Asian");
		menuItem.setDinnerDelivery("Dine-In");
		menuItem.setIsActive(true);
		menuItem.setUserId(testProfile.getId());
		menuDataService.saveOrUpdateMenuItem(menuItem);
		List<MenuItem> ms = menuRepository.findAll();
		assertThat(ms, notNullValue());
	}
	
	@Test
	@Rollback
	@Ignore
	public void testUpdateListing() throws Exception {
		MenuItemVO menuItem = new MenuItemVO();
		menuItem.setTitle("Veg Fried Rice");
		menuItem.setDescription("Veg Fried Rice");
		menuItem.setDinnerCategories("Asian");
		menuItem.setDinnerDelivery("Dine-In");
		menuItem.setIsActive(true);
		menuItem.setUserId(testProfile.getId());
		MenuItem m =  menuDataService.saveOrUpdateMenuItem(menuItem);
		assertThat(m, notNullValue());
		MenuItemVO menuItemToUpdate = new MenuItemVO();
		BeanUtils.copyProperties(menuItem, menuItemToUpdate);
		menuItemToUpdate.setDescription("Spicy Veg Fried Rice");
		menuDataService.saveOrUpdateMenuItem(menuItemToUpdate);
		List<MenuItem> ms = menuDataService.findMenuByProfileId(testProfile.getId());
		assertThat(ms, notNullValue());
		MenuItem m1 = menuDataService.findMenuByTitle("Veg Fried Rice", testProfile.getId());
		assertThat(m1.getDescription(), equalTo("Spicy Veg Fried Rice"));
	}
}
