/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.domain.vo;

import com.p2pdinner.repositories.MenuRepository;
import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.MenuItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author rajani@p2pdinner.com
 */
public class DinnerListingEntityBuilder implements EntityBuilder<DinnerListing, DinnerListingVO> {

    @Autowired
    private MenuRepository menuItemRepository;

    public DinnerListing build(DinnerListingVO r) throws Exception {
        if (r == null) {
            return null;
        }
        DinnerListing dinnerListing = new DinnerListing();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTime startDate = formatter.parseDateTime(r.getStartTime());
        DateTime endDate = formatter.parseDateTime(r.getEndTime());
        DateTime closeDate = formatter.parseDateTime(r.getCloseTime());
        dinnerListing.setStartTime(new java.sql.Timestamp(startDate.toDate().getTime()));
        dinnerListing.setCloseTime(new java.sql.Timestamp(closeDate.toDate().getTime()));
        dinnerListing.setEndTime(new java.sql.Timestamp(endDate.toDate().getTime()));
        BeanUtils.copyProperties(r, dinnerListing, new String[] { "startTime", "endTime", "closeTime"});
        MenuItem menuItem = menuItemRepository.findOne(r.getMenuItemId());
        dinnerListing.setMenuItem(menuItem);
        return dinnerListing;
    }

}
