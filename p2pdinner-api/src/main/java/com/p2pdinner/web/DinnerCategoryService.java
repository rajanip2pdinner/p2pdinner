package com.p2pdinner.web;

import com.p2pdinner.service.P2PDataService;
import com.p2pdinner.domain.DinnerCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Path("/dinnercategory")
public class DinnerCategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DinnerCategoryService.class);
	
	@Autowired
	private P2PDataService p2pDataService;
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDinnerCategory(DinnerCategory dinnerCategory) {
		DinnerCategory category = p2pDataService.saveDinnerCategory(dinnerCategory);
		return Response.ok(category).build();
	}
	
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewDinnerCategories() {
		Collection<DinnerCategory> categories = p2pDataService.getAllDinnerCategories();
		List<DinnerCategory> categoryList = new ArrayList<DinnerCategory>(categories);
		Collections.sort(categoryList, new Comparator<DinnerCategory>(){

			@Override
			public int compare(DinnerCategory o1, DinnerCategory o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return Response.ok(categoryList).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response categories() {
		return viewDinnerCategories();
	}
	
}
