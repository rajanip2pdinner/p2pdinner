package com.p2p.data.service;

import java.util.Collection;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.p2p.data.config.P2PTestContextConfiguration;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.vo.DinnerListingVO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DatabaseSetup("/dinnerlisting-data-service-test-data.xml")
@Transactional
public class DinnerListingDataServiceTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DinnerListingDataServiceTest.class);
	
	@Autowired
	private DinnerListingDataService dinnerListingDataService;

	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	@Test
	public void testUpdateAvailability() throws Exception {
		LOGGER.info("Test updateAvailability");
		DinnerListingVO dinnerListingVO = new DinnerListingVO();
		dinnerListingVO.setStartTime(DateTime.now().plusDays(1).toString(dateTimeFormatter));
		dinnerListingVO.setEndTime(DateTime.now().plusDays(5).toString(dateTimeFormatter));
		dinnerListingVO.setCloseTime(DateTime.now().plusDays(1).minusHours(2).toString(dateTimeFormatter));
		dinnerListingVO.setMenuItemId(15);
		dinnerListingVO.setCostPerItem(7.99);
		dinnerListingVO.setAvailableQuantity(10);
		DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
		Assert.assertNotNull(listing.getId());
		Assert.assertNotNull(listing.getMenuItem());
	}

	@Test
	public void testUpdateAvailabilityWithInvalidStartDate() throws Exception {
		LOGGER.info("testUpdateAvailabilityWithInvalidStartDate");
		DinnerListingVO dinnerListingVO = new DinnerListingVO();
		dinnerListingVO.setStartTime(DateTime.now().plusDays(1).toString(dateTimeFormatter));
		dinnerListingVO.setEndTime(DateTime.now().minusDays(5).toString(dateTimeFormatter));
		dinnerListingVO.setCloseTime(DateTime.now().plusDays(1).minusHours(2).toString(dateTimeFormatter));
		dinnerListingVO.setMenuItemId(15);
		dinnerListingVO.setCostPerItem(7.99);
		dinnerListingVO.setAvailableQuantity(10);
		try {
			DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
		} catch (P2PDinnerException pe) {
			Assert.assertTrue(pe.getErrorCode().equals(ErrorCode.INVALID_START_TIME.getErrorCode()));
		}
	}

	@Test
	public void testUpdateAvailabilityWithInvalidCloseDate() throws Exception {
		LOGGER.info("testUpdateAvailabilityWithInvalidStartDate");
		DinnerListingVO dinnerListingVO = new DinnerListingVO();
		dinnerListingVO.setStartTime(DateTime.now().plusDays(1).toString(dateTimeFormatter));
		dinnerListingVO.setEndTime(DateTime.now().plusDays(5).toString(dateTimeFormatter));
		dinnerListingVO.setCloseTime(DateTime.now().plusDays(5).plusHours(2).toString(dateTimeFormatter));
		dinnerListingVO.setMenuItemId(15);
		dinnerListingVO.setCostPerItem(7.99);
		dinnerListingVO.setAvailableQuantity(10);
		try {
			DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
		} catch (P2PDinnerException pe) {
			Assert.assertTrue(pe.getErrorCode().equals(ErrorCode.INVALID_CLOSE_TIME.getErrorCode()));
		}
	}
	
	@Test
	public void testCurrentListings() throws Exception {
		LOGGER.info("Test CurrentListings");
		DinnerListingVO dinnerListingVO = new DinnerListingVO();
		dinnerListingVO.setStartTime(new DateTime().plus(Period.minutes(60)).toString(dateTimeFormatter));
		dinnerListingVO.setEndTime(new DateTime().plus(Period.minutes(60 * 2)).toString(dateTimeFormatter));
		dinnerListingVO.setCloseTime(new DateTime().plus(Period.minutes(45)).toString(dateTimeFormatter));
		dinnerListingVO.setMenuItemId(15);
		dinnerListingVO.setCostPerItem(7.99);
		dinnerListingVO.setAvailableQuantity(10);
		dinnerListingDataService.updateAvailability(dinnerListingVO);
		Collection<DinnerListing> listings = dinnerListingDataService.currentListings();
		Assert.assertNotNull(listings);
		Assert.assertTrue(listings.size() > 0);
		for(DinnerListing listing : listings) {
			LOGGER.debug("##### -> {}", ToStringBuilder.reflectionToString(listing));
		}
	}

	@Test
	public void testUpdateAvailabilityInvalidAddress() throws Exception {
		LOGGER.info("Test updateAvailability");
		DinnerListingVO dinnerListingVO = new DinnerListingVO();
		dinnerListingVO.setStartTime(DateTime.now().plusDays(1).toString(dateTimeFormatter));
		dinnerListingVO.setEndTime(DateTime.now().plusDays(5).toString(dateTimeFormatter));
		dinnerListingVO.setCloseTime(DateTime.now().plusDays(1).minusHours(2).toString(dateTimeFormatter));
		dinnerListingVO.setMenuItemId(16);
		dinnerListingVO.setCostPerItem(7.99);
		dinnerListingVO.setAvailableQuantity(10);
		try {
			DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
		} catch (P2PDinnerException pe) {
			Assert.assertTrue(pe.getErrorCode().equalsIgnoreCase(ErrorCode.INVALID_ADDRESS.getErrorCode()));
		}
	}
}
