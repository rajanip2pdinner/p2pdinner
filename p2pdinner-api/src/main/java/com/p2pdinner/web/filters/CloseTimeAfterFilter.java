package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rajaniy on 02/29/16.
 */
@FilterMapper(keyword = "after_close_time")
public class CloseTimeAfterFilter implements Predicate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseTimeAfterFilter.class);

    private DateTime dateTime;

    public CloseTimeAfterFilter(String date) {
        dateTime = DateTime.parse(date, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
    }

    @Override
    public boolean evaluate(Object o) {
        if ( o instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) o;
            DateTime listingCloseTime = new DateTime(dinnerListing.getCloseTime());
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC();
            LOGGER.info("Dinner Listing Item : {}", dinnerListing.getMenuItem().getTitle());
            LOGGER.info("Dinner Listing close Time  {}", formatter.print(listingCloseTime));
            LOGGER.info("Query Time {}", formatter.print(dateTime));
            if (listingCloseTime.isAfter(dateTime)) {
                LOGGER.info(" <<FILTER> CloseTimeFilterAfter returning true");
                return true;
            }
        }
        return false;
    }
}
