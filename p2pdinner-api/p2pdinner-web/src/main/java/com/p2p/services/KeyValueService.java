package com.p2p.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.p2p.data.service.P2PDataService;
import com.p2p.domain.KeyValue;

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
