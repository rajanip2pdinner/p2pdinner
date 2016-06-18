package com.p2p.domain;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dinner_cart")
public class DinnerCart implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SequenceGenerator(name = "DINNER_CART_SEQ", allocationSize = 25)
	@Id @GeneratedValue(generator = "DINNER_CART_SEQ")
	@Column(name = "cart_id")
	private Integer id;
	
	@ManyToOne
	private UserProfile buyer;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdDate;
	
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modifiedDate;
	
	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<DinnerCartItem> cartItems;
	
	@Column(name = "pass_code")
	private String passCode;
	
	@Column(name = "stripe_charge_id")
	private String chargeId;


	@Column(name = "delivery_type")
	private String deliveryType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserProfile getBuyer() {
		return buyer;
	}

	public void setBuyer(UserProfile buyer) {
		this.buyer = buyer;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Set<DinnerCartItem> getCartItems() {
		if (cartItems == null) {
			cartItems = new HashSet<DinnerCartItem>();
		}
		return cartItems;
	}

	public void setCartItems(Set<DinnerCartItem> cartItems) {
		this.cartItems = cartItems;
	}
	
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	
	public String getPassCode() {
		return this.passCode;
	}
	
	public String getChargeId() {
		return chargeId;
	}
	
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
}
