package com.p2p.data.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.data.repositories.DinnerCartRepository;
import com.p2p.domain.DinnerCart;
import com.p2p.setup.DinnerCartBaseTest;

@Transactional
public class DinnerCartDataServiceTest extends DinnerCartBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DinnerCartDataServiceTest.class);
	
	@Autowired
	private DinnerCartDataService dinnerCartDataService;
	
	@Autowired
	private DinnerCartRepository dinnerCartRepository;
	
	@Test
	public void testSaveOrUpdateDinnerCart() throws Exception {
		String request = "{\"listing_id\" : 11, \"profile_id\" : 11, \"quantity\" : 5, \"delivery_type\" : \"To-Go\" }";
		DinnerCart savedCart = dinnerCartDataService.saveOrUpdateCart(request);
		assertThat(savedCart, notNullValue());
		assertThat(savedCart.getId(), notNullValue());
	}
	
	@Test
	public void testSaveOrUpdateDinnerCartMultipleItems() throws Exception {
		String request = "{\"listing_id\" : 11, \"profile_id\" : 11, \"quantity\" : 5 , \"delivery_type\" : \"To-Go\"}";
		DinnerCart savedCart = dinnerCartDataService.saveOrUpdateCart(request);
		request = "{\"listing_id\" : 11, \"profile_id\" : 11, \"quantity\" : 5" + ",\"cart_id\": " + savedCart.getId() + ", \"delivery_type\" : \"To-Go\"  }";
		savedCart = dinnerCartDataService.saveOrUpdateCart(request);
		assertThat(savedCart, notNullValue());
		assertThat(savedCart.getId(), notNullValue());
//		assertThat(savedCart.getCartItems().size(), equalTo(2));
	}
	
	@Test
	public void testCountOrderQuanity() throws Exception {
		int count = dinnerCartDataService.countOrderedQuantityByListingId(11);
		System.out.println("Order Quantity : " + count);
		assertThat(count, not(0));
	}
	
	@Test
	public void testCartBySeller() throws Exception {
		List<Object[]> carts = dinnerCartDataService.getReceivedOrderBySellerProfile(11);
		assertThat(carts, notNullValue());
	}
	
	@Test
	public void testOrderByDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(sdf.parse("05/05/2015 00:00:00"));
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(sdf.parse("05/15/2015 00:00:00"));
		List<Object[]> carts = dinnerCartDataService.receivedOrdersByDate(startDate, endDate);
		assertThat(carts, notNullValue());
		assertThat(carts, hasSize(1));
	}
}
