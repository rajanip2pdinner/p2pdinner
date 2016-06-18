package com.p2p.domain.vo;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class AddressEntityBuilderTest {

	@Test
	public void testBuild() throws Exception {
		String response = "{ \"results\": [ ], \"status\": \"ZERO_RESULTS\"};";
		EntityBuilder<Address, String> addressEntityBuilder = new AddressEntityBuilder();
		Address address = addressEntityBuilder.build(response);
		assertThat(address, nullValue());
	}
}
