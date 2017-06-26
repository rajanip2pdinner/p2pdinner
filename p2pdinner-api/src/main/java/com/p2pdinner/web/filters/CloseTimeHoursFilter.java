package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FilterMapper(keyword = "close_time_hours")
public class CloseTimeHoursFilter implements Predicate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseTimeHoursFilter.class);
	
	private Integer hours;
	
	public CloseTimeHoursFilter(String hours) {
		this.hours = Integer.valueOf(hours);
	}

	@Override
	public boolean evaluate(Object o) {
		if (o instanceof DinnerListing) {
			DinnerListing dinnerListing = (DinnerListing) o;
			DateTime listingCloseTime = new DateTime(dinnerListing.getCloseTime(), DateTimeZone.UTC);
			DateTime startCloseTime = DateTime.now(DateTimeZone.UTC);
			DateTime endCloseTime = DateTime.now(DateTimeZone.UTC).plusHours(hours);
			return (listingCloseTime.isAfter(startCloseTime) && listingCloseTime.isBefore(endCloseTime));
		}
		return false;
	}

}
