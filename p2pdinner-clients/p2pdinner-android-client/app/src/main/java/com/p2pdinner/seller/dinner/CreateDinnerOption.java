package com.p2pdinner.seller.dinner;

/**
 * Created by rajaniy on 7/27/15.
 */
public class CreateDinnerOption {
    private String title;
    private boolean clickable;
    private String subTitle;
    private Long itemId;

    public CreateDinnerOption(String title, boolean clickable, String subTitle, Long itemId) {
        this.title = title;
        this.clickable = clickable;
        this.subTitle = subTitle;
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
