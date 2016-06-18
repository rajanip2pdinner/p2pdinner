package com.p2p.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212393921 on 2/1/16.
 */
@Provider
public class SecurityExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", Response.Status.OK.getStatusCode());
        response.put("code", "unauthorized");
        response.put("message", exception.getMessage());
        return Response.ok(response).build();
    }
}
