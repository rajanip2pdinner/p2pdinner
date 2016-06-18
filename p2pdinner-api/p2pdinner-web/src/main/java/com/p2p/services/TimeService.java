package com.p2p.services;

import com.p2p.models.Time;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeService {

    @GET
    public Response get() {
       return Response.ok(new Time()).build();
    }

}

