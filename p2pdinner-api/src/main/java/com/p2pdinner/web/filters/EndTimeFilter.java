package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by rajaniy on 8/29/15.
 */
@FilterMapper(keyword = "end_time", requireMultipleValues = false)
public class EndTimeFilter implements Predicate {

    private DateTime dateTime;

    public EndTimeFilter(String date) {
        dateTime =  DateTime.parse(date, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));
    }

    @Override
    public boolean evaluate(Object o) {
        if ( o instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) o;
            DateTime listingStartTime = new DateTime(dinnerListing.getEndTime());
            if (listingStartTime.isAfter(dateTime)) {
                return true;
            }
        }
        return false;
    }
}
