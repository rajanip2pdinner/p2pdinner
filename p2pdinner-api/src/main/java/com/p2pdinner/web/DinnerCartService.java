package com.p2pdinner.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.exceptions.P2PDinnerDataException;
import com.p2pdinner.service.DinnerCartDataService;
import com.p2pdinner.service.MenuDataService;
import com.p2pdinner.domain.DinnerCart;
import com.p2pdinner.domain.DinnerCartItem;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.OrderStatus;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.domain.vo.OrderedItemDetailVO;
import com.p2pdinner.domain.vo.ValueResponse;
import com.p2pdinner.utils.ResponseHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Path("/cart")
public class DinnerCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DinnerCartService.class);

    @Autowired
    private EntityBuilder<DinnerCart, String> entityBuilder;

    @Autowired
    private DinnerCartDataService dinnerCartDataService;

    @Autowired
    private MenuDataService menuDataService;

    @Autowired
    private ExceptionMessageBuilder excepBuilder;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addItem(String request) throws P2PDinnerException {
        DinnerCart cart = null;
        try {
            cart = dinnerCartDataService.saveOrUpdateCart(request);
        } catch (P2PDinnerDataException pdex) {
            throw excepBuilder.createException(ErrorCode.ADD_CART_FAILED, new Object[]{pdex.getReason().name(), pdex.getMessage()}, Locale.US);
        } catch (P2PDinnerException pe) {
            LOGGER.error(pe.getMessage(), pe);
            throw pe;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{e.getMessage()}, Locale.US);
        }
        ValueResponse response = new ValueResponse();
        response.addKey("status", Status.OK.name());
        response.addKey("cartId", cart.getId());
        return Response.ok(response).build();
    }

    @Path("/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer cartId, String requestBody) throws P2PDinnerException {
        LOGGER.info("Request Body --- {}", requestBody);
        try {
            DinnerCart dinnerCart = dinnerCartDataService.getCart(cartId);
            if (dinnerCart == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            JsonElement jsonElement = new JsonParser().parse(requestBody);
            JsonObject rootObject  = jsonElement.getAsJsonObject();
            if (rootObject.has("buyer_rating")) {
                Integer buyerRating = rootObject.get("buyer_rating").getAsInt();
                dinnerCart.setBuyerRating(buyerRating);
            }
            if (rootObject.has("seller_rating")) {
                Integer sellerRating = rootObject.get("seller_rating").getAsInt();
                dinnerCart.setSellerRating(sellerRating);
            }
            dinnerCartDataService.update(dinnerCart);
            ValueResponse response = new ValueResponse();
            response.addKey("status", Status.OK.name());
            response.addKey("message", "Cart updated successfully");
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{"Failed to retrieve cart items"}, Locale.US);
        }
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cart(@PathParam("id") Integer cartId) throws P2PDinnerException {
        try {
            DinnerCart dinnerCart = dinnerCartDataService.getCart(cartId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", Status.OK.name());
            Map<String, Object> results = new HashMap<>();
            Map<Integer, DinnerCartItem> itemsMap = new HashMap<>();
            if (dinnerCart.getCartItems() != null && !dinnerCart.getCartItems().isEmpty()) {
                for (DinnerCartItem dci : dinnerCart.getCartItems()) {
                    if (!itemsMap.containsKey(dci.getId())) {
                        itemsMap.put(dci.getId(), dci);
                    }
                }
                List<Map<String, Object>> cartItemsList = new ArrayList<>();
                Double totalPrice = 0d;
                for (DinnerCartItem dci : itemsMap.values()) {
                    Map<String, Object> cartItemsMap = new HashMap<>();
                    cartItemsMap.put("cart_id", dci.getDinnerCart().getId());
                    cartItemsMap.put("cart_item_id", dci.getId());
                    cartItemsMap.put("title", dci.getDinnerListing().getMenuItem().getTitle());
                    cartItemsMap.put("description", dci.getDinnerListing().getMenuItem().getDescription());
                    cartItemsMap.put("price", dci.getDinnerListing().getCostPerItem());
                    cartItemsMap.put("total_price", dci.getTotalPrice());
                    cartItemsMap.put("quantity", dci.getOrderQuantity());
                    totalPrice += dci.getTotalPrice();
                    cartItemsList.add(cartItemsMap);
                }
                results.put("results", cartItemsList);
                results.put("total_price", totalPrice);
            }
            return Response.ok(results).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{"Failed to retrieve cart items"}, Locale.US);
        }
    }

    @Path("/items/{cartId}/count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartItemCount(@PathParam("cartId") Integer cartId) {
        ValueResponse response = new ValueResponse();
        response.addKey("status", Status.OK.name());
        response.addKey("count", dinnerCartDataService.getCartItemCount(cartId));
        return Response.ok(response).build();
    }

    @Path("/items/{cartId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartItems(@PathParam("cartId") Integer cartId) throws Exception {
        String response = ResponseHelper.generateResponse(dinnerCartDataService.getCart(cartId));
        return Response.ok(response).build();
    }

    @Path("/items/profile/{profileId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartItemsByProfile(@PathParam("profileId") Integer profileId) throws Exception {
        DinnerCart dinnerCart = dinnerCartDataService.getCartByProfileId(profileId);
        if (dinnerCart == null) {
            Map<String, Object> responseEntity = new HashMap<String, Object>();
            responseEntity.put("status", "OK");
            responseEntity.put("results", Collections.EMPTY_LIST);
            responseEntity.put("total_price", 0);
            return Response.ok(responseEntity).build();
        }
        String response = ResponseHelper.generateResponse(dinnerCartDataService.getCartByProfileId(profileId));
        return Response.ok(response).build();
    }

    @Path("/placeorder/{profileId}/{cartId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(@PathParam("profileId") Integer profileId, @PathParam("cartId") Integer cartId, String request) throws P2PDinnerException {
        try {
            LOGGER.info("Request : {}", request);
            dinnerCartDataService.placeOrder(profileId, cartId, OrderStatus.ORDER_IN_PROGRESS);
        } catch (P2PDinnerException de) {
            throw de;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{request}, Locale.US);
        }
        ValueResponse response = new ValueResponse();
        response.addKey("status", Status.OK.name());
        response.addKey("message", "Your order is placed and you will receive a notification containing more information");
        return Response.ok(response).build();
    }

    @Path("/received/{profileId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recievedOrders(@PathParam("profileId") Integer profileId) throws P2PDinnerException {
        ValueResponse response = new ValueResponse();
        try {
            List<Object[]> carts = dinnerCartDataService.getReceivedOrderBySellerProfile(profileId);
            Map<Integer, List<Map<String, Object>>> receivedOrders = new HashMap<Integer, List<Map<String, Object>>>();
            for (Object[] cart : carts) {
                if (cart != null && cart.length != 0) {
                    if (cart[0] instanceof DinnerCart) {
                        DinnerCart dc = (DinnerCart) cart[0];
                        if (cart[1] instanceof DinnerCartItem) {
                            DinnerCartItem dci = (DinnerCartItem) cart[1];
                            if (receivedOrders.containsKey(dc.getId())) {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                dciMap.put("title", dci.getDinnerListing().getMenuItem().getTitle());
                                dciMap.put("description", dci.getDinnerListing().getMenuItem().getDescription());
                                dciMap.put("cost_per_item", dci.getDinnerListing().getCostPerItem());
                                dciMap.put("order_quantity", dci.getOrderQuantity());
                                dciMap.put("total_price", dci.getTotalPrice());
                                dciMap.put("pass_code", dc.getPassCode());
                                List<Map<String, Object>> items = receivedOrders.get(dc.getId());
                                items.add(dciMap);
                            } else {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                dciMap.put("title", dci.getDinnerListing().getMenuItem().getTitle());
                                dciMap.put("description", dci.getDinnerListing().getMenuItem().getDescription());
                                dciMap.put("cost_per_item", dci.getDinnerListing().getCostPerItem());
                                dciMap.put("order_quantity", dci.getOrderQuantity());
                                dciMap.put("total_price", dci.getTotalPrice());
                                dciMap.put("pass_code", dc.getPassCode());
                                List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
                                items.add(dciMap);
                                receivedOrders.put(dc.getId(), items);
                            }
                        }
                    }
                }
            }
            if (carts == null || carts.isEmpty()) {
                response.addKey("status", Status.OK.name());
                response.addKey("results", Collections.EMPTY_LIST);
            } else {
                response.addKey("status", Status.OK.name());
                response.addKey("results", receivedOrders.values());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{e.getMessage()}, Locale.US);
        }
        return Response.ok(response).build();
    }

    @Path("/received")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recievedOrders(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) throws P2PDinnerException {
        ValueResponse response = new ValueResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
        try {
            Calendar stDate = Calendar.getInstance();
            stDate.setTime(sdf.parse(startDate));
            Calendar enDate = Calendar.getInstance();
            enDate.setTime(sdf.parse(endDate));
            List<Object[]> carts = dinnerCartDataService.receivedOrdersByDate(stDate, enDate);
            Map<Integer, List<Map<String, Object>>> receivedOrders = new HashMap<Integer, List<Map<String, Object>>>();
            for (Object[] cart : carts) {
                if (cart != null && cart.length != 0) {
                    if (cart[0] instanceof DinnerCart) {
                        DinnerCart dc = (DinnerCart) cart[0];
                        if (cart[1] instanceof DinnerCartItem) {
                            DinnerCartItem dci = (DinnerCartItem) cart[1];
                            if (receivedOrders.containsKey(dc.getId())) {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = receivedOrders.get(dc.getId());
                                items.add(dciMap);
                            } else {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
                                items.add(dciMap);
                                receivedOrders.put(dc.getId(), items);
                            }
                        }
                    }
                }
            }
            if (carts == null || carts.isEmpty()) {
                response.addKey("status", Status.OK.name());
                response.addKey("results", Collections.EMPTY_LIST);
            } else {
                response.addKey("status", Status.OK.name());
                response.addKey("results", receivedOrders.values());
            }
        } catch (ParseException pe) {
            LOGGER.error(pe.getMessage(), pe);
            throw excepBuilder.createException(ErrorCode.BAD_REQUEST, null, Locale.US);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{e.getMessage()}, Locale.US);
        }
        return Response.ok(response).build();
    }

    @Path("/received/d/{profileId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recievedOrders(@PathParam("profileId") Integer profileId, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) throws P2PDinnerException {
        ValueResponse response = new ValueResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
        try {
            Calendar stDate = Calendar.getInstance();
            stDate.setTime(sdf.parse(startDate));
            Calendar enDate = Calendar.getInstance();
            enDate.setTime(sdf.parse(endDate));
            List<Object[]> carts = dinnerCartDataService.receivedOrdersByProfileAndDate(profileId, stDate, enDate);
            Map<Integer, List<Map<String, Object>>> receivedOrders = new HashMap<Integer, List<Map<String, Object>>>();
            for (Object[] cart : carts) {
                if (cart != null && cart.length != 0) {
                    if (cart[0] instanceof DinnerCart) {
                        DinnerCart dc = (DinnerCart) cart[0];
                        if (cart[1] instanceof DinnerCartItem) {
                            DinnerCartItem dci = (DinnerCartItem) cart[1];
                            if (receivedOrders.containsKey(dc.getId())) {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = receivedOrders.get(dc.getId());
                                items.add(dciMap);
                            } else {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
                                items.add(dciMap);
                                receivedOrders.put(dc.getId(), items);
                            }
                        }
                    }
                }
            }
            if (carts == null || carts.isEmpty()) {
                response.addKey("status", Status.OK.name());
                response.addKey("results", Collections.EMPTY_LIST);
            } else {
                response.addKey("status", Status.OK.name());
                response.addKey("results", receivedOrders.values());
            }
        } catch (ParseException pe) {
            LOGGER.error(pe.getMessage(), pe);
            throw excepBuilder.createException(ErrorCode.BAD_REQUEST, null, Locale.US);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{e.getMessage()}, Locale.US);
        }
        return Response.ok(response).build();
    }

    @Path("/received/tier/{tier}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recievedOrdersByTier(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @PathParam("tier") String tier) throws P2PDinnerException {
        ValueResponse response = new ValueResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
        try {
            Calendar stDate = Calendar.getInstance();
            stDate.setTime(sdf.parse(startDate));
            Calendar enDate = Calendar.getInstance();
            enDate.setTime(sdf.parse(endDate));
            List<Object[]> carts = dinnerCartDataService.receivedOrdersByDateAndTier(stDate, enDate, tier);
            Map<Integer, List<Map<String, Object>>> receivedOrders = new HashMap<Integer, List<Map<String, Object>>>();
            for (Object[] cart : carts) {
                if (cart != null && cart.length != 0) {
                    if (cart[0] instanceof DinnerCart) {
                        DinnerCart dc = (DinnerCart) cart[0];
                        if (cart[1] instanceof DinnerCartItem) {
                            DinnerCartItem dci = (DinnerCartItem) cart[1];
                            if (receivedOrders.containsKey(dc.getId())) {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = receivedOrders.get(dc.getId());
                                items.add(dciMap);
                            } else {
                                Map<String, Object> dciMap = new HashMap<String, Object>();
                                prepare(dc, dci, dciMap);
                                List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
                                items.add(dciMap);
                                receivedOrders.put(dc.getId(), items);
                            }
                        }
                    }
                }
            }
            if (carts == null || carts.isEmpty()) {
                response.addKey("status", Status.OK.name());
                response.addKey("results", Collections.EMPTY_LIST);
            } else {
                response.addKey("status", Status.OK.name());
                response.addKey("results", receivedOrders.values());
            }
        } catch (ParseException pe) {
            LOGGER.error(pe.getMessage(), pe);
            throw excepBuilder.createException(ErrorCode.BAD_REQUEST, null, Locale.US);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{e.getMessage()}, Locale.US);
        }
        return Response.ok(response).build();
    }

    @Path("/orders/{listingId}/received/detail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderItemDetails(@PathParam("listingId") Integer listingId) {
        List<OrderedItemDetailVO> orderedItemDetailVOs = dinnerCartDataService.recievedOrderItemDetail(listingId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", Status.OK.name());
        response.put("results", orderedItemDetailVOs);
        return Response.ok(response).build();
    }

    @Path("/orders/{profileId}/{passCode}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderDetailsByProfileAndPassCode(@PathParam("profileId") Integer profileId, @PathParam("passCode") String passCode) {
        List<OrderedItemDetailVO> orderedItemDetailVOs = dinnerCartDataService.receivedOrdersByBuyerProfileAndPassCode(profileId, passCode);
        Map<String, Object> response = new HashMap<>();
        response.put("status", Status.OK.name());
        response.put("results", orderedItemDetailVOs);
        return Response.ok(response).build();
    }

    @Path("/placedorders/{profileId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersByDate(@PathParam("profileId") Integer buyerProfileId, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
        DateTime startTime = DateTime.parse(startDate, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
        DateTime endTime = DateTime.parse(endDate, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").withZoneUTC());
        List<OrderedItemDetailVO> orderedItemDetailVOs = dinnerCartDataService.receivedOrdersByBuyerProfileAndDate(buyerProfileId, startTime, endTime);
        Map<String, Object> response = new HashMap<>();
        response.put("status", Status.OK.name());
        response.put("results", orderedItemDetailVOs);
        return Response.ok(response).build();
    }

    private void prepare(DinnerCart dc, DinnerCartItem dci, Map<String, Object> dciMap) {
        if (StringUtils.isEmpty(dc.getPassCode())) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        dciMap.put("title", dci.getDinnerListing().getMenuItem().getTitle());
        dciMap.put("description", dci.getDinnerListing().getMenuItem().getDescription());
        dciMap.put("cost_per_item", dci.getDinnerListing().getCostPerItem());
        dciMap.put("order_quantity", dci.getOrderQuantity());
        dciMap.put("total_price", dci.getTotalPrice());
        dciMap.put("pass_code", dc.getPassCode());
        dciMap.put("stripe_charge_id", dc.getChargeId());
        dciMap.put("available_quantity", dci.getDinnerListing().getAvailableQuantity());
        MenuItem menuItem = menuDataService.findMenuItemById(dci.getDinnerListing().getMenuItem().getId());
        dciMap.put("seller_stripe_customer_id", menuItem.getUserProfile().getStripeCustomerId());
        dciMap.put("tier", menuItem.getUserProfile().getTier());
        dciMap.put("cart_item_id", menuItem.getId());
        dciMap.put("order_received_date", sdf.format(dc.getCreatedDate().getTime()));
        dciMap.put("listing_id", dci.getDinnerListing().getId());
    }
}
