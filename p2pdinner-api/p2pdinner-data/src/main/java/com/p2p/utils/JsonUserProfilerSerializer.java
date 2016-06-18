package com.p2p.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.util.StringUtils;

import com.p2p.domain.UserProfile;

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
