package com.p2p.domain.vo;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.p2p.domain.DinnerCart;
import com.p2p.setup.DinnerCartBaseTest;

@Transactional
public class DinnerCartEntityBuilderTest extends DinnerCartBaseTest {
	
	@Autowired
	private EntityBuilder<DinnerCart, String> entityBuilder;
	
	@Test
	@Rollback
	public void testBuild() throws Exception {
		String request = "{\"listing_id\" : 11, \"profile_id\" : 11, \"quantity\" : 5, \"delivery_type\" : \"To-Go\" }";
		DinnerCart cart = entityBuilder.build(request);
		Assert.assertNotNull(cart);
	}
}
