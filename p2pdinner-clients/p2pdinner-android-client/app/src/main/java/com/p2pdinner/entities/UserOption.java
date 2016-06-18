package com.p2pdinner.entities;

import java.io.Serializable;

/**
 * Created by rajaniy on 7/18/15.
 */
public class UserOption implements Serializable {
    private int icon;
    private String title;
    private String shortDescription;

    public UserOption(int icon, String title, String shortDescription) {
        this.icon = icon;
        this.title = title;
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
