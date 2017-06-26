package com.p2pdinner.domain.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ValueResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String,Object> response;
	
	public Map<String,Object> getResponse() {
		return response;
	}
	
	public void addKey(String key, Object object) {
		if ( response == null) {
			response = new HashMap<String, Object>();
		}
		response.put(key, object);
	}
}
