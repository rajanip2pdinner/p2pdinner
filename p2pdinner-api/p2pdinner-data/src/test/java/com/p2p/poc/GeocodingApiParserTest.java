package com.p2p.poc;

import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeocodingApiParserTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeocodingApiParserTest.class);
	@Test
	@Ignore
	public void testParser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = getClass().getResourceAsStream("/geocode.json");
		JsonNode rootNode = mapper.readTree(inputStream);
		JsonNode otherNode = rootNode.get("results");
		for(final JsonNode node : otherNode) {
			JsonNode geometryNode = node.path("geometry").path("location");
			System.out.println(geometryNode);
		}
	}
	
	@Test
	public void testJodaTimeUTC() throws Exception  {
		DateTime dt = new DateTime(DateTimeZone.UTC);
		LOGGER.info("### Date format: {} - Long format : {}", dt, dt.toDate().getTime());
		DateTime startOfDay = new DateTime(DateTimeZone.UTC).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		LOGGER.info("### Date format: {} - Long format : {}", startOfDay, startOfDay.toDate().getTime());
	}
}
