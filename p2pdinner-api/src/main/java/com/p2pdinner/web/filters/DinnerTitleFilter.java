package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;

@FilterMapper(keyword = "title", requireMultipleValues = false)
public class DinnerTitleFilter implements Predicate {
	
	private static final String keyword = "title";
	private String arg;
	
	public DinnerTitleFilter(String arg) {
		this.arg = arg;
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			if (listing != null && listing.getMenuItem().getTitle().toLowerCase().contains(arg.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
