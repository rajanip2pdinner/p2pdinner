package com.p2pdinner.web;

import com.p2pdinner.service.P2PDataService;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Path("/specialneed")
public class DinnerSpecialNeedService {
private static final Logger LOGGER = LoggerFactory.getLogger(DinnerSpecialNeedService.class);
	
	@Autowired
	private P2PDataService p2pDataService;
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDinnerSpecialNeed(DinnerSpecialNeeds dinnerDelivery) {
		DinnerSpecialNeeds delivery = p2pDataService.saveDinnerSpecialNeed(dinnerDelivery);
		return Response.ok(delivery).build();
	}
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewSpecialNeeds() {
		Collection<DinnerSpecialNeeds> dinnerSpecialNeeds = p2pDataService.getAllDinnerSpecialNeeds();
		List<DinnerSpecialNeeds> specialNeedsList = new ArrayList<>(dinnerSpecialNeeds);
		Collections.sort(specialNeedsList, new Comparator<DinnerSpecialNeeds>() {
			@Override
			public int compare(DinnerSpecialNeeds o1, DinnerSpecialNeeds o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return Response.ok(specialNeedsList).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response specialNeeds() {
		return viewSpecialNeeds();
	}
	
}
