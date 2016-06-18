package com.p2p.poc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by rajaniy on 12/23/15.
 */
public class DateTimeTest {
    private static final Logger _logger = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void testCase1(){
        LocalDateTime dateTime = LocalDateTime.now();
        for(int i = 0; i < 5; i++) {
            _logger.info(dateTime.toString());
            dateTime = dateTime.minus(Period.hours(24));
        }
    }

    @Test
    public void testCase2() {
        String date = "04/22/2016 04:00:00";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTime dateTime = formatter.withZoneUTC().parseDateTime(date);
        System.out.println(formatter.withZone(DateTimeZone.getDefault()).print(dateTime));
    }

}
