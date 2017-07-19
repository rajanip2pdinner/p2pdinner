package com.p2pdinner.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.p2pdinner.domain.UserProfile;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonUserProfilerSerializer extends JsonSerializer<UserProfile> {

	@Override
	public void serialize(UserProfile userProfile, JsonGenerator jgen, SerializerProvider sp) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		
		if (!StringUtils.isEmpty(userProfile.getId())) {
			jgen.writeFieldName("id");
			jgen.writeNumber(userProfile.getId());
		}

		if (!StringUtils.isEmpty(userProfile.getEmailAddress())) {
			jgen.writeFieldName("emailAddress");
			jgen.writeString(userProfile.getEmailAddress());
		}

		if (!StringUtils.isEmpty(userProfile.getAddressLine1())) {
			jgen.writeFieldName("addressLine1");
			jgen.writeString(userProfile.getAddressLine1());
		}

		if (!StringUtils.isEmpty(userProfile.getAddressLine2())) {
			jgen.writeFieldName("addressLine2");
			jgen.writeString(userProfile.getAddressLine2());
		}

		if (!StringUtils.isEmpty(userProfile.getCity())) {
			jgen.writeFieldName("city");
			jgen.writeString(userProfile.getCity());
		}

		if (!StringUtils.isEmpty(userProfile.getLatitude())) {
			jgen.writeFieldName("latitude");
			jgen.writeNumber(userProfile.getLatitude());
		}

		if (!StringUtils.isEmpty(userProfile.getLongitude())) {
			jgen.writeFieldName("longitude");
			jgen.writeNumber(userProfile.getLongitude());
		}
		jgen.writeEndObject();
	}

}
