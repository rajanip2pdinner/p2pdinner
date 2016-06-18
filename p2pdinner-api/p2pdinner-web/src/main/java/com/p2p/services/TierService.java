package com.p2p.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.data.service.TierDataService;
import com.p2p.domain.Tier;

@Service
@Path("/tiers")
public class TierService {
	@Autowired
	private ExceptionMessageBuilder excepBuilder;
	
	@Autowired
	private TierDataService tierDataService;

	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response tiers() {
		Collection<Tier> tiers = tierDataService.tiers();
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("status", HttpStatus.OK.name());
		List<Map<String,Object>> tierList = new ArrayList<Map<String,Object>>();
		for(Tier tier : tiers) {
			Map<String,Object> tierObjectMap = new HashMap<String,Object>();
			tierObjectMap.put("tier_name", tier.getTierName());
			tierObjectMap.put("tier_id", tier.getId());
			tierObjectMap.put("rate", tier.getRate());
			tierObjectMap.put("pay_interval", tier.getPayInterval());
			tierList.add(tierObjectMap);
		}
		response.put("tiers", tierList);
		return Response.ok(response).build();
	}
}
