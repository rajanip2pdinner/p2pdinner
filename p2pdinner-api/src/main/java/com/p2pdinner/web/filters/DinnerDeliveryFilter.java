package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@FilterMapper(keyword = "delivery", requireMultipleValues = true)
public class DinnerDeliveryFilter implements Predicate {
	
	private String[] args;

	@InitMethod
	public void setArgs(String... args) {
		this.args = args;
	}

	@Override
	public boolean evaluate(Object object) {
		if (object instanceof DinnerListing) {
			DinnerListing listing = (DinnerListing) object;
			List<String> deliveries = new ArrayList<String>();
			if (listing != null && !listing.getMenuItem().getDinnerCategories().isEmpty()) {
				for(DinnerDelivery dinnerDelivery : listing.getMenuItem().getDinnerDeliveries()) {
					deliveries.add(dinnerDelivery.getName());
				}
				ContainsPredicate p = new ContainsPredicate(StringUtils.collectionToCommaDelimitedString(deliveries));
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
