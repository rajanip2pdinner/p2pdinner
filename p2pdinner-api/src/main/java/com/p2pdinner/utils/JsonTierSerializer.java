package com.p2pdinner.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.p2pdinner.domain.Tier;

import java.io.IOException;

public class JsonTierSerializer extends JsonSerializer<Tier> {

	@Override
	public void serialize(Tier value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("tier_name", value.getTierName());
		jgen.writeNumberField("tier_id", value.getId());
		jgen.writeNumberField("rate", value.getRate());
		jgen.writeNumberField("pay_interval", value.getPayInterval());
		jgen.writeEndObject();
	}

}
