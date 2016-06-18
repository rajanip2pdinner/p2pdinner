package com.p2p.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.p2p.domain.DinnerCategory;
import com.p2p.domain.DinnerDelivery;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.DinnerSpecialNeeds;

public class JsonDinnerListingSerializer extends JsonSerializer<DinnerListing> {

	@Override
	public void serialize(DinnerListing listing, JsonGenerator generator, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		generator.writeStartObject();
		generator.writeFieldName("title");
		generator.writeString(listing.getMenuItem().getTitle());
		generator.writeFieldName("description");
		generator.writeString(listing.getMenuItem().getDescription());
		generator.writeNumberField("profile_id", listing.getMenuItem().getUserProfile().getId());
		generator.writeStringField("name", listing.getMenuItem().getUserProfile().getEmailAddress());
		generator.writeArrayFieldStart("categories");
		for(DinnerCategory category : listing.getMenuItem().getDinnerCategories()) {
			generator.writeString(category.getName());
		}
		generator.writeEndArray();
		generator.writeArrayFieldStart("special_needs");
		for(DinnerSpecialNeeds specialNeed : listing.getMenuItem().getDinnerSpecialNeeds()) {
			generator.writeString(specialNeed.getName());
		}
		generator.writeEndArray();
		generator.writeArrayFieldStart("delivery");
		for(DinnerDelivery delivery : listing.getMenuItem().getDinnerDeliveries()) {
			generator.writeString(delivery.getName());
		}
		generator.writeEndArray();
		generator.writeObjectFieldStart("listing");
		generator.writeNumberField("listing_id", listing.getId());
		generator.writeNumberField("cost_per_item", listing.getCostPerItem());
		generator.writeNumberField("quantity_available", listing.getAvailableQuantity());
		generator.writeNumberField("ordered_quantity", listing.getOrderQuantity());
		generator.writeStringField("start_time", sdf.format(listing.getStartTime()));
		generator.writeStringField("close_time", sdf.format(listing.getCloseTime()));
		generator.writeStringField("end_date", sdf.format(listing.getEndTime()));
		generator.writeEndObject();
		generator.writeEndObject();
	}


}
