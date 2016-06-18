package com.p2p.services;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.p2p.data.service.P2PDataService;
import com.p2p.domain.DinnerDelivery;

@Service
@Path("/delivery")
public class DinnerDeliveryService {
private static final Logger LOGGER = LoggerFactory.getLogger(DinnerDeliveryService.class);
	
	@Autowired
	private P2PDataService p2pDataService;
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDinnerDelivery(DinnerDelivery dinnerDelivery) {
		DinnerDelivery delivery = p2pDataService.saveDinnerDelivery(dinnerDelivery);
		return Response.ok(delivery).build();
	}
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewDinnerDeliveries() {
		Collection<DinnerDelivery> dinnerDeliveries = p2pDataService.getAllDinnerDeliveries();
		List<DinnerDelivery> dinnerDeliveryList = new ArrayList<>(dinnerDeliveries);
		Collections.sort(dinnerDeliveryList, new Comparator<DinnerDelivery>() {
			@Override
			public int compare(DinnerDelivery o1, DinnerDelivery o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return Response.ok(dinnerDeliveryList).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deliveryOptions() {
		return viewDinnerDeliveries();
	}
	
}
