package com.p2p.filters;

import com.p2p.domain.DinnerCategory;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.MenuItem;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DinnerListingFilterProcessorTestContext.class})
public class DinnerListingFilterProcessorTest {

    @Autowired
    private DinnerListingFilterProcessor filterProcessor;

    private List<DinnerListing> dinnerListingList;

    @Before
    public void setUp() {
        dinnerListingList = new ArrayList<>();
        DinnerListing listing = new DinnerListing();
        listing.setId(1);
        DateTime startTime = DateTime.now();
        startTime = startTime.minusDays(5);
        DateTime endTime = DateTime.now();
        endTime = endTime.plusDays(5);
        listing.setEndTime(new java.sql.Timestamp(endTime.toDate().getTime()));
        listing.setStartTime(new java.sql.Timestamp(startTime.toDate().getTime()));
        listing.setCostPerItem(4.99d);
        listing.setAvailableQuantity(50);
        DinnerCategory category1 = new DinnerCategory();
        category1.setName("Indian");
        DinnerCategory category2 = new DinnerCategory();
        category2.setName("Korean");
        HashSet<DinnerCategory> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);
        MenuItem mi = new MenuItem();
        mi.setDinnerCategories(categories);
        listing.setMenuItem(mi);
        dinnerListingList.add(listing);
    }

    @After
    public void tearDown() {
        dinnerListingList.clear();
    }

    @Test
    public void testMaxPriceFilter() throws Exception {
        String qry = "max_price:5.99";
        Collection<DinnerListing> listings = filterProcessor.applyFilters(qry, dinnerListingList);
        assertThat(listings, notNullValue());
        assertThat(listings.isEmpty(), is(false));

    }

    @Test
    public void testMinPriceFilter() throws Exception {
        String qry = "min_price:3.99";
        Collection<DinnerListing> listings = filterProcessor.applyFilters(qry, dinnerListingList);
        assertThat(listings, notNullValue());
        assertThat(listings.isEmpty(), is(false));

    }

    @Test
    public void testCategoriesFilter() throws Exception {
        String qry = "category:indian,korean";
        Collection<DinnerListing> listings = filterProcessor.applyFilters(qry, dinnerListingList);
        assertThat(listings, notNullValue());
        assertThat(listings.isEmpty(), is(false));

    }
}
