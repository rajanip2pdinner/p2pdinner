/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.web;

import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.service.DinnerListingDataService;
import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.transformers.DinnerListingTransformer;
import com.p2pdinner.domain.vo.DinnerListingVO;
import com.p2pdinner.domain.vo.P2PResponseBuilder;
import com.p2pdinner.domain.vo.P2PResponseBuilderImpl;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author rajani
 */
@Service
@Path("/listing")
public class DinnerListingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DinnerListingService.class);

	@Autowired
	private DinnerListingDataService dinnerListingDataService;
	
	@Autowired
	private ExceptionMessageBuilder excepBuilder;

	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addListing(DinnerListingVO dinnerListingVO) throws P2PDinnerException {
		Response response;
		LOGGER.info("Adding listing {}", ReflectionToStringBuilder.toString(dinnerListingVO));
		try {
			DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
			P2PResponseBuilder<DinnerListing, DinnerListingVO> responseBuilder = new P2PResponseBuilderImpl<>();
			DinnerListingVO listingVO = new DinnerListingVO();
			listingVO = responseBuilder.buildResponse(listing, listingVO, new DinnerListingTransformer());
			response = Response.ok(listingVO).build();
		} catch (P2PDinnerException p2pException) {
			LOGGER.error(p2pException.getMessage(), p2pException);
			throw p2pException;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage()}, Locale.US);
		}
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(DinnerListingVO dinnerListingVO) throws P2PDinnerException {
		Response response;
		LOGGER.info("Adding listing {}", ReflectionToStringBuilder.toString(dinnerListingVO));
		try {
			DinnerListing listing = dinnerListingDataService.updateAvailability(dinnerListingVO);
			P2PResponseBuilder<DinnerListing, DinnerListingVO> responseBuilder = new P2PResponseBuilderImpl<>();
			DinnerListingVO listingVO = new DinnerListingVO();
			listingVO = responseBuilder.buildResponse(listing, listingVO, new DinnerListingTransformer());
			response = Response.ok(listingVO).build();
		} catch (P2PDinnerException p2pException) {
			LOGGER.error(p2pException.getMessage(), p2pException);
			throw p2pException;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage()}, Locale.US);
		}
		return response;
	}

	@Path("/view/current")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response currentListings() {
		Response response;
		try {
			response = Response.ok(dinnerListingDataService.currentListings()).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		return response;
	}
	
	@Path("/view/current/{profileId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response currentListingsByProfile(@PathParam("profileId") Integer profileId) throws P2PDinnerException {
		Response response;
		try {
			Collection<DinnerListing> currentListings = dinnerListingDataService.currentListings(profileId);
			if ( currentListings == null || currentListings.isEmpty()) {
				throw excepBuilder.createException(ErrorCode.NO_LISTINGS, null, Locale.US);
			}
			response = Response.ok(currentListings).build();
		} catch (P2PDinnerException pe) {
			LOGGER.error(pe.getMessage(), pe);
			throw pe;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage()}, Locale.US);
		}
		return response;
	}
	
	@Path("/view/{profileId}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response viewListingsByDate(@PathParam("profileId") Integer profileId, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) throws P2PDinnerException {
		Response response;
		try {
			DateTime startTime = DateTime.parse(startDate, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
			DateTime endTime = DateTime.parse(endDate, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
			Collection<DinnerListing> currentListings = dinnerListingDataService.listingByDate(profileId, startTime, endTime);
			if ( currentListings == null || currentListings.isEmpty()) {
				throw excepBuilder.createException(ErrorCode.NO_LISTINGS, null, Locale.US);
			}
			response = Response.ok(currentListings).build();
		} catch (P2PDinnerException pe) {
			LOGGER.error(pe.getMessage(), pe);
			throw pe;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage()}, Locale.US);
		}
		return response ;
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listingDetail(@PathParam("id")Integer listingId) throws  P2PDinnerException  {
		try {
			DinnerListing listing = dinnerListingDataService.listingById(listingId);
			Map<String,Object> responseMap = new HashMap<>();
			responseMap.put("status", Status.OK.name());
			responseMap.put("results", listing);
			return  Response.ok(responseMap).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] { e.getMessage()}, Locale.US);
		}
	}


}
