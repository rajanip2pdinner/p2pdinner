package com.p2p.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "key_value")
public class KeyValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "KEY_VALUE_SEQ", allocationSize = 25)
	@Id @GeneratedValue(generator = "KEY_VALUE_SEQ")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "k")
	private String key;

	@Column(name = "v")
	private String value;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

}
