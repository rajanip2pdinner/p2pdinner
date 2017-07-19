package com.p2pdinner.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.p2pdinner.utils.JsonCalendarSerializer;
import com.p2pdinner.utils.JsonTierSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;


@Entity
@Table(name = "tier")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = JsonTierSerializer.class)
public class Tier implements Serializable{
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "TIER_SEQ", allocationSize = 25)
	@Id
	@GeneratedValue(generator = "TIER_SEQ")
	@Column(name = "tier_id")
	private Integer id;
	
	@Column(name = "tier_name")
	private String tierName;
	
	@Column(name = "commission_rate")
	private Float rate;
	
	@Column(name = "pay_interval")
	private Integer payInterval;
	
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar createdDate;
	
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar modifiedDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonCalendarSerializer.class)
	private Calendar endDate;
	
	public Tier(){
		this.payInterval = 2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
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

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	public String getTierName() {
		return tierName;
	}
	
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}
	
	public Integer getPayInterval() {
		return payInterval;
	}
	
	public void setPayInterval(Integer payInterval) {
		this.payInterval = payInterval;
	}
}
