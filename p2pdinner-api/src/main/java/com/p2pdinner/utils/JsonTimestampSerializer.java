package com.p2pdinner.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class JsonTimestampSerializer extends JsonSerializer<Timestamp> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	@Override
	public void serialize(Timestamp timestamp, JsonGenerator gen, SerializerProvider sp) throws IOException,
			JsonProcessingException {
		gen.writeString(DATE_FORMAT.format(timestamp));
	}

}
