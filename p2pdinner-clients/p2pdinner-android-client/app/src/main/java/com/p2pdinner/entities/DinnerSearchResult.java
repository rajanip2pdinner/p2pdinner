package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by rajaniy on 11/16/15.
 */
public class DinnerSearchResult implements Serializable {
    @SerializedName("profile_id")
    private Integer profileId;
    @SerializedName("menu_items")
    private Set<DinnerMenuItem> menuItems;

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Set<DinnerMenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<DinnerMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
