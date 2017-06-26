package com.p2pdinner.domain;

import javax.persistence.*;

@Entity
@Table(name = "dinner_special_needs")
public class DinnerSpecialNeeds extends DinnerParentEntity {
	private static final long serialVersionUID = 1L;
	
	@SequenceGenerator(name = "DINNER_SPECIAL_NEEDS_SEQ", allocationSize = 25)
	@Id @GeneratedValue(generator = "DINNER_SPECIAL_NEEDS_SEQ")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
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
	
}
