package com.p2p.filters;

import org.apache.commons.collections.Predicate;

/**
 * Created by rajaniy on 8/29/15.
 */

public class ContainsPredicate implements Predicate {

    private String category;

    public ContainsPredicate(String category) {
        this.category = category;
    }

    @Override
    public boolean evaluate(Object o) {
        if (o instanceof String) {
            if (category.toLowerCase().contains(o.toString())) {
                return true;
            }
        }
        return false;
    }
}

