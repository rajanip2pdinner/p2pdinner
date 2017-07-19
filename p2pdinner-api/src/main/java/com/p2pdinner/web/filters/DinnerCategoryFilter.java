package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@FilterMapper(keyword = "category", requireMultipleValues = true)
public class DinnerCategoryFilter implements Predicate {
	
	private String[] args;

	@InitMethod
	public void setArgs(String... args) {
		this.args = args;
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			List<String> categories = new ArrayList<String>();
			if (listing != null && !listing.getMenuItem().getDinnerCategories().isEmpty()) {
				for(DinnerCategory c : listing.getMenuItem().getDinnerCategories()) {
					categories.add(c.getName());
				}
				ContainsPredicate p = new ContainsPredicate(StringUtils.collectionToCommaDelimitedString(categories));
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
