package com.p2p.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "dinner_cart_item")
public class DinnerCartItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SequenceGenerator(name = "DINNER_CART_ITEM_SEQ", allocationSize = 25)
	@Id @GeneratedValue(generator = "DINNER_CART_ITEM_SEQ")
	@Column(name = "cart_item_id")
	private Integer id;

	@ManyToOne
	private DinnerListing dinnerListing;
	
	@ManyToOne
	@JsonIgnore
	private DinnerCart dinnerCart;
	
	@Column(name = "order_quantity")
	private Integer orderQuantity;
	
	@Column(name = "total_price")
	private Double totalPrice;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DinnerListing getDinnerListing() {
		return dinnerListing;
	}

	public void setDinnerListing(DinnerListing dinnerListing) {
		this.dinnerListing = dinnerListing;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public DinnerCart getDinnerCart() {
		return dinnerCart;
	}
	
	public void setDinnerCart(DinnerCart dinnerCart) {
		this.dinnerCart = dinnerCart;
	}
}
