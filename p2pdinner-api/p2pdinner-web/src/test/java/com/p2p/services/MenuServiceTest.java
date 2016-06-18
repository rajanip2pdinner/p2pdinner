package com.p2p.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p2p.data.service.MenuDataService;
import com.p2p.domain.MenuItem;
import com.p2p.domain.vo.MenuItemVO;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class MenuServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceTest.class);
	
	@InjectMocks
	private MenuService menuService;
	
	@Mock
	private MenuDataService menuDataService;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(menuDataService.saveOrUpdateMenuItem(Mockito.any(MenuItemVO.class))).thenReturn(new MenuItem());
	}
	
	@Test
	public void testAddMenuItem() {
		//Response response = menuService.addMenuItem(new MenuItemVO(), null);
		//assertThat(response, notNullValue());
		//assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
	}
}
