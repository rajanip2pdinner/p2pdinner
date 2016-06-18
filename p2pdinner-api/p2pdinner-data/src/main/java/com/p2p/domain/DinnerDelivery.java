package com.p2p.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "dinner_delivery")
public class DinnerDelivery extends DinnerParentEntity {
	private static final long serialVersionUID = 1L;
	

	@SequenceGenerator(name = "DINNER_DELIVERY_SEQ", allocationSize = 25)
	@Id @GeneratedValue(generator = "DINNER_DELIVERY_SEQ")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "dinner_listing_delivery", joinColumns = @JoinColumn(name = "dinner_delivery_id"), inverseJoinColumns = @JoinColumn(name = "dinner_listing_id"))
	private Collection<MenuItem> dinnerListings = new ArrayList<MenuItem>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<MenuItem> getDinnerListings() {
		return dinnerListings;
	}
	
	public void setDinnerListings(Collection<MenuItem> dinnerListings) {
		this.dinnerListings = dinnerListings;
	}

}
