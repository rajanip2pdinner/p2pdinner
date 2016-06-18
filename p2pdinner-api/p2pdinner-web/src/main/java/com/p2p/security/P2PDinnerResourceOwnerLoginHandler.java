package com.p2p.security;

import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;

public class P2PDinnerResourceOwnerLoginHandler implements ResourceOwnerLoginHandler {

	@Override
	public UserSubject createSubject(String name, String password) {
		return new UserSubject(name, password);
	}

}
