package com.p2pdinner.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JsonCalendarSerializer extends JsonSerializer<Calendar> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public void serialize(Calendar timestamp, JsonGenerator gen, SerializerProvider sp) throws IOException,
			JsonProcessingException {
		gen.writeString(DATE_FORMAT.format(timestamp.getTime()));
	}

}
