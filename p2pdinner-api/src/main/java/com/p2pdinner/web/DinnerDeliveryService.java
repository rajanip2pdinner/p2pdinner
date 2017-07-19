package com.p2pdinner.web;

import com.p2pdinner.service.P2PDataService;
import com.p2pdinner.domain.DinnerDelivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

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
