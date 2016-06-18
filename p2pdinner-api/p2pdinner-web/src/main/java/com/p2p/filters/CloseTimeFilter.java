package com.p2p.filters;

import com.p2p.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rajaniy on 02/29/16.
 */
@FilterMapper(keyword = "close_time")
public class CloseTimeFilter implements Predicate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseTimeFilter.class);

    private DateTime dateTime;

    public CloseTimeFilter(String date) {
        dateTime = DateTime.parse(date, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
    }

    @Override
    public boolean evaluate(Object o) {
        if ( o instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) o;
            DateTime listingCloseTime = new DateTime(dinnerListing.getCloseTime(), DateTimeZone.UTC);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC();
            LOGGER.info("Dinner Listing close Time  {}", formatter.print(listingCloseTime));
            LOGGER.info("Query Time {}", formatter.print(dateTime));
            if (listingCloseTime.isAfter(dateTime)) {
                LOGGER.info(" <<FILTER> CloseTimeFilter returning true");
                return true;
            }
        }
        return false;
    }
}
