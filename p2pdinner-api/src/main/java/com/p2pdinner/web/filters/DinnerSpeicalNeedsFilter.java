package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import org.apache.commons.collections.Predicate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@FilterMapper(keyword = "special_needs", requireMultipleValues = true)
public class DinnerSpeicalNeedsFilter implements Predicate {
	
	private String[] args;

	@InitMethod
	public void setArgs(String... args) {
		this.args = args;
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			List<String> specialNeeds = new ArrayList<String>();
			if (listing != null && !listing.getMenuItem().getDinnerSpecialNeeds().isEmpty()) {
				for(DinnerSpecialNeeds dinnerSpecialNeeds : listing.getMenuItem().getDinnerSpecialNeeds()) {
					specialNeeds.add(dinnerSpecialNeeds.getName());
				}
				ContainsPredicate p = new ContainsPredicate(StringUtils.collectionToCommaDelimitedString(specialNeeds));
				for(String arg : args) {
					boolean contains = p.evaluate(arg);
					if (contains) {
						return contains;
					}
				}

			}
		}
		return false;
	}

}
