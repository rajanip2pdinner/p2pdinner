package com.p2pdinner.web;


import com.p2pdinner.domain.Time;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
@Service
public class TimeService {

    @GET
    public Response get() {
       return Response.ok(new Time()).build();
    }

}

