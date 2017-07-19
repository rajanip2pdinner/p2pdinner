package com.p2pdinner.domain;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.p2pdinner.utils.JsonTimestampSerializer;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

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
