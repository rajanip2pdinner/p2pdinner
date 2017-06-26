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
@FilterMapper(keyword = "before_close_time")
public class CloseTimeBeforeFilter implements Predicate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseTimeBeforeFilter.class);

    private DateTime dateTime;

    public CloseTimeBeforeFilter(String date) {
        dateTime = DateTime.parse(date, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
    }

    @Override
    public boolean evaluate(Object o) {
        if ( o instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) o;
            DateTime listingCloseTime = new DateTime(dinnerListing.getCloseTime());
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC();
            LOGGER.info("Dinner Listing close Time  {}", formatter.print(listingCloseTime));
            LOGGER.info("Query Time {}", formatter.print(dateTime));
            if (listingCloseTime.isBefore(dateTime)) {
                LOGGER.info(" <<FILTER> CloseTimeFilterBefore returning true");
                return true;
            }
        }
        return false;
    }
}
