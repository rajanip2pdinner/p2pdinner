package com.p2pdinner.domain.vo;

import com.p2pdinner.domain.MenuItem;

import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<MenuItem> menuItems;
	private Double latitude;
	private Double longitude;
	
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
