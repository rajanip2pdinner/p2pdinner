package com.p2p.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonTimestampSerializer extends JsonSerializer<java.sql.Timestamp> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	@Override
	public void serialize(java.sql.Timestamp timestamp, JsonGenerator gen, SerializerProvider sp) throws IOException,
			JsonProcessingException {
		gen.writeString(DATE_FORMAT.format(timestamp));
	}

}
