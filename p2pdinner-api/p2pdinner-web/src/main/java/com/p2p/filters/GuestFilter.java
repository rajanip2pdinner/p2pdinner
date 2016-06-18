package com.p2p.filters;

import com.p2p.domain.DinnerListing;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rajaniy on 12/21/15.
 */
@FilterMapper(keyword = "guests", requireMultipleValues = false)
public class GuestFilter implements Predicate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestFilter.class);

    private String arg;

    public GuestFilter(String arg) {
        this.arg = arg;
    }

    @Override
    public boolean evaluate(Object object) {
        if (object instanceof DinnerListing) {
            DinnerListing dinnerListing = (DinnerListing) object;
            Integer guests = Integer.parseInt(arg);
            if (dinnerListing != null && dinnerListing.getAvailableQuantity() >= guests) {
                LOGGER.info("<<FILTER>> GuestFilter returning true");
                return true;
            }
        }
        return false;
    }
}
