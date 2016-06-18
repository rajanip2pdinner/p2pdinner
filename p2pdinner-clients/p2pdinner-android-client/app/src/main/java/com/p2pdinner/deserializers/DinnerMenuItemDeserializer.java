package com.p2pdinner.deserializers;

import com.google.gson.*;
import com.p2pdinner.entities.DinnerListing;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.Location;


import org.springframework.util.StringUtils;

import java.lang.reflect.Type;

/**
 * Created by rajaniy on 11/16/15.
 */
public class DinnerMenuItemDeserializer implements JsonDeserializer<DinnerMenuItem> {
    public DinnerMenuItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final DinnerMenuItem dinnerMenuItem = new DinnerMenuItem();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final String title = jsonObject.get("title").getAsString();
        String description = null;
        if (jsonObject.get("description") != null && jsonObject.get("description") != JsonNull.INSTANCE) {
            description = jsonObject.get("description").getAsString();
        }
        final Long profileId = jsonObject.get("profile_id").getAsLong();
        final String name = jsonObject.get("name").getAsString();
        final String profileName = jsonObject.get("profile_name").getAsString();

        if (jsonObject.get("image_uri") != null && jsonObject.get("image_uri") != JsonNull.INSTANCE) {
            dinnerMenuItem.setImageUri(jsonObject.get("image_uri").getAsString());
        }


        final JsonArray categories = jsonObject.getAsJsonArray("categories");
        StringBuffer cats = new StringBuffer();
        for(JsonElement elem : categories) {
            cats.append(elem.getAsString()).append(",");
        }
        final JsonArray specialNeeds = jsonObject.getAsJsonArray("special_needs");
        StringBuffer snds = new StringBuffer();
        for(JsonElement elem : specialNeeds) {
            snds.append(elem.getAsString()).append(",");
        }
        final JsonArray delivery = jsonObject.getAsJsonArray("delivery");
        StringBuffer dels = new StringBuffer();
        for(JsonElement elem : delivery) {
            dels.append(elem.getAsString()).append(",");
        }
        final DinnerListing dinnerListing = jsonDeserializationContext.deserialize(jsonObject.get("listing"), DinnerListing.class);
        dinnerMenuItem.setTitle(title);
        dinnerMenuItem.setDescription(description);
        dinnerMenuItem.setProfileId(profileId);
        if (StringUtils.hasText(profileName)) {
            dinnerMenuItem.setProfileName(profileName);
        } else {
            dinnerMenuItem.setProfileName("");
        }
        if (StringUtils.hasText(cats.toString())) {
            dinnerMenuItem.setCategories(cats.substring(0, cats.length() - 1));
        }
        if (StringUtils.hasText(snds.toString())) {
            dinnerMenuItem.setSpecialNeeds(snds.substring(0, snds.length() - 1));
        }
        if (StringUtils.hasText(dels.toString())) {
            dinnerMenuItem.setDeliveryOptions(dels.substring(0, dels.length() - 1));
        }


        dinnerMenuItem.setDinnerListing(dinnerListing);

        final Location location = jsonDeserializationContext.deserialize(jsonObject.get("location"), Location.class);
        dinnerMenuItem.setLocation(location);
        return dinnerMenuItem;
    }
}
