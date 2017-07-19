package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rajaniy on 8/29/15.
 */
@FilterMapper(keyword = "start_time")
public class StartTimeFilter implements Predicate {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTimeFilter.class);

    private DateTime dateTime;

    public StartTimeFilter(String date) {
        dateTime = DateTime.parse(date, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
    }

    @Override
    public boolean evaluate(Object o) {
        if ( o instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) o;
            DateTime listingStartTime = new DateTime(dinnerListing.getStartTime(), DateTimeZone.UTC);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC();
            LOGGER.info("Dinner Listing start Time  {}", formatter.print(listingStartTime));
            LOGGER.info("Query Time {}", formatter.print(dateTime));
            if (listingStartTime.isAfter(dateTime)) {
                LOGGER.info(" <<FILTER> StartTimeFilter returning true");
                return true;
            }
        }
        return false;
    }
}
