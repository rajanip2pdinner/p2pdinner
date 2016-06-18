package com.p2p.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class JsonCalendarDeserializer extends JsonDeserializer<Calendar> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String date = jp.getText();
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(DATE_FORMAT.parse(date));
		} catch (Exception ex) {
			calendar = null;
		}
		return calendar;
	}

}
