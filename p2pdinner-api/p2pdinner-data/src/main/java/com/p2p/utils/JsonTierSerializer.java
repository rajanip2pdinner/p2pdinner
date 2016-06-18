package com.p2p.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.p2p.domain.Tier;

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
