package com.p2p.filters;

import org.springframework.context.annotation.Bean;

public class DinnerListingFilterProcessorTestContext {
	@Bean
	public DinnerListingFilterProcessor dinnerListingFilterProcessor(){
		return new DinnerListingFilterProcessor();
	}
}
