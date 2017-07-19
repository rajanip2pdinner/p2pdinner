package com.p2pdinner.domain.vo;

import java.io.Serializable;

public class DinnerCartItemVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String title;
	private Double cost;
	private Integer quantity;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
