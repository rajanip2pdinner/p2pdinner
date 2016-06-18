package com.p2p.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.p2p.utils.JsonTimestampSerializer;

@MappedSuperclass
public class DinnerParentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "start_date")
	private java.sql.Timestamp startDate;

	@Column(name = "end_date")
	private java.sql.Timestamp endDate;

	@Column(name = "modified_date")
	private java.sql.Timestamp modifiedDate;

	@Column(name = "is_active")
	private Boolean isActive;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	public java.sql.Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Timestamp startDate) {
		this.startDate = startDate;
	}

	@JsonSerialize(using = JsonTimestampSerializer.class)
	public java.sql.Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Timestamp endDate) {
		this.endDate = endDate;
	}

	@JsonSerialize(using = JsonTimestampSerializer.class)
	public java.sql.Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	

}
