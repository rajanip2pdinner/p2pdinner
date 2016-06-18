package com.p2p.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonCalendarSerializer extends JsonSerializer<Calendar> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public void serialize(Calendar timestamp, JsonGenerator gen, SerializerProvider sp) throws IOException,
			JsonProcessingException {
		gen.writeString(DATE_FORMAT.format(timestamp.getTime()));
	}

}
