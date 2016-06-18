package com.p2p.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.p2p.common.exceptions.P2PDinnerException;

public class P2PDinnerExceptionMapper implements ExceptionMapper<P2PDinnerException> {
	public Response toResponse(P2PDinnerException exception) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", Status.OK.getStatusCode());
		response.put("code", exception.getErrorCode());
		response.put("message", exception.getMessage());
		return Response.ok(response).build();
	}

}
