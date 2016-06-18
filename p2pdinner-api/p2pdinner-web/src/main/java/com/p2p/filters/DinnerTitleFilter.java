package com.p2p.filters;

import org.apache.commons.collections.Predicate;

import com.p2p.domain.DinnerListing;

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
