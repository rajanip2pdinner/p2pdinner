/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2p.domain.vo;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
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
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.p2p.data.config.P2PTestContextConfiguration;
import com.p2p.domain.DinnerListing;

/**
 *
 * @author rajani
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("/dinnerlisting-data.xml")
public class DinnerListingEntityBuilderTest {
	
	@Autowired
	private EntityBuilder<DinnerListing, DinnerListingVO> dinnerListingEntityBuilder;
    
    @Test
    public void testBuild() throws Exception {
        DinnerListingVO dinnerListingVO = new DinnerListingVO();
        dinnerListingVO.setMenuItemId(10);
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MINUTE, 60);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        dinnerListingVO.setStartTime(DateTime.now().toString(dateTimeFormatter));
        dinnerListingVO.setEndTime(DateTime.now().toString(dateTimeFormatter));
        dinnerListingVO.setCloseTime(DateTime.now().toString(dateTimeFormatter));
        dinnerListingVO.setAvailableQuantity(5);
        DinnerListing dinnerListing = dinnerListingEntityBuilder.build(dinnerListingVO);
        Assert.assertNotNull(dinnerListing);
        Assert.assertTrue(dinnerListing.getAvailableQuantity() == 5);
    }
    
}
