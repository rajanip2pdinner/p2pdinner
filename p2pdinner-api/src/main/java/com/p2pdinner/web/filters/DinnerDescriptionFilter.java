package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;

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
