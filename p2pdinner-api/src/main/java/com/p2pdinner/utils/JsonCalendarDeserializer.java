package com.p2pdinner.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
