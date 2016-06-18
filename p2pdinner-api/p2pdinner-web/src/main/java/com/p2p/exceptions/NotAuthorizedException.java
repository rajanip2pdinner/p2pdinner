package com.p2p.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebFault;

/**
 * Created by 212393921 on 2/1/16.
 */
@WebFault
public class NotAuthorizedException extends WebApplicationException {
    public NotAuthorizedException(String fault) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(fault).type(MediaType.APPLICATION_JSON).build());
    }
}
