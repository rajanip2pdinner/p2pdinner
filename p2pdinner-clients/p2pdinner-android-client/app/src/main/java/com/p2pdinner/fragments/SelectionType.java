package com.p2pdinner.fragments;

/**
 * Created by rajaniy on 6/20/16.
 */

public enum SelectionType {
    COST_PER_PLATE("Set Cost Per Plate"),
    NO_OF_ITEMS("Set Available Quantity");

    private String title;

    SelectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
