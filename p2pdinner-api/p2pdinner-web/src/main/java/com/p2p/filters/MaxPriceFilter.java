package com.p2p.filters;

import org.apache.commons.collections.Predicate;

import com.p2p.domain.DinnerListing;

@FilterMapper(keyword = "max_price")
public class MaxPriceFilter implements Predicate {
	
	private Double arg;
	
	public MaxPriceFilter(String arg) {
		this.arg = Double.parseDouble(arg);
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			if (listing != null && listing.getCostPerItem() < this.arg) {
				return true;
			}
		}
		return false;
	}

}
