package com.p2pdinner.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dinner_cart")
public class DinnerCart implements Serializable {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "DINNER_CART_SEQ", allocationSize = 25)
	@Id
	@GeneratedValue(generator = "DINNER_CART_SEQ")
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

	@Column(name = "buyer_rating")
	private Integer buyerRating;

	@Column(name = "seller_rating")
	private Integer sellerRating;

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

	public Integer getBuyerRating() {
		return buyerRating;
	}

	public void setBuyerRating(Integer buyerRating) {
		this.buyerRating = buyerRating;
	}

	public Integer getSellerRating() {
		return sellerRating;
	}

	public void setSellerRating(Integer sellerRating) {
		this.sellerRating = sellerRating;
	}
}