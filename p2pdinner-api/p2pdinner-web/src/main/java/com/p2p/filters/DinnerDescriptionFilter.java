package com.p2p.filters;

import org.apache.commons.collections.Predicate;

import com.p2p.domain.DinnerListing;

@FilterMapper(keyword = "description", requireMultipleValues = false)
public class DinnerDescriptionFilter implements Predicate {
	
	private String arg;
	
	public DinnerDescriptionFilter(String arg) {
		this.arg = arg;
	}
	
	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			if (listing != null && listing.getMenuItem().getDescription().toLowerCase().contains(arg.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
