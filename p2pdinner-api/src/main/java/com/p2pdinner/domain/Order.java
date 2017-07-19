package com.p2pdinner.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dinner_orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@SequenceGenerator(name = "ORDER_SEQ", allocationSize = 25)
	@GeneratedValue(generator = "ORDER_SEQ")
	@Id
	private Integer id;
	
	@Column(name = "transaction_id")
	private Long transactionId;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "buyer_profile_id")
	private UserProfile buyer;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "seller_profile_id")
	private UserProfile seller;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "dinner_listing_id")
	private DinnerListing dinnerListing;
	
	@Column(name = "pass_code")
	private String passCode;
	
	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Column(name = "order_quantity")
	private Integer quantity;

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

	public UserProfile getSeller() {
		return seller;
	}

	public void setSeller(UserProfile seller) {
		this.seller = seller;
	}

	public DinnerListing getDinnerListing() {
		return dinnerListing;
	}

	public void setDinnerListing(DinnerListing dinnerListing) {
		this.dinnerListing = dinnerListing;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Long getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

}
