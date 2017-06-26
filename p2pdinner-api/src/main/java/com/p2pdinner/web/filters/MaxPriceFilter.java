package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;

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
