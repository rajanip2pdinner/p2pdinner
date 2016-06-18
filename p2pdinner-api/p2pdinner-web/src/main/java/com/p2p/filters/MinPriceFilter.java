package com.p2p.filters;

import org.apache.commons.collections.Predicate;

import com.p2p.domain.DinnerListing;

@FilterMapper(keyword = "min_price")
public class MinPriceFilter implements Predicate {
	
	private Double arg;
	
	public MinPriceFilter(String arg) {
		this.arg = Double.parseDouble(arg);
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			if (listing != null && listing.getCostPerItem() > this.arg) {
				return true;
			}
		}
		return false;
	}

}
