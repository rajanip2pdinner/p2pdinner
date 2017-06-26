package com.p2pdinner.web;

import com.p2pdinner.service.P2PDataService;
import com.p2pdinner.domain.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;

@Path("/keyvalue")
public class KeyValueService {
	
	@Autowired
	private P2PDataService p2pDataService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addKeyValue(KeyValue keyValue) {
		Response response = Response.ok().build();
		if ( keyValue == null) {
			response = Response.serverError().status(Status.BAD_REQUEST).build();
		} else {
			KeyValue savedKeyValue = p2pDataService.saveKeyValue(keyValue);
			response = Response.ok(savedKeyValue).build();
		}
		
		return response;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/view")
	public Response viewAll(){
		Response response = Response.ok().build();
		Collection<KeyValue> allKeyValues = p2pDataService.findAllKeyValues();
		response = Response.ok(allKeyValues).build();
		return response;
	}
}
