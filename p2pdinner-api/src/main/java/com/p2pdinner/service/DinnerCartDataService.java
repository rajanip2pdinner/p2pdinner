package com.p2pdinner.service;


import com.p2pdinner.domain.DinnerCart;
import com.p2pdinner.domain.DinnerCartItem;
import com.p2pdinner.domain.OrderStatus;
import com.p2pdinner.domain.vo.EntityBuilder;
import com.p2pdinner.domain.vo.OrderedItemDetailVO;
import com.p2pdinner.repositories.DinnerCartItemRepository;
import com.p2pdinner.repositories.DinnerCartRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Component
public class DinnerCartDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DinnerCartDataService.class);

    @Resource
    private DinnerCartRepository dinnerCartRepository;

    @Autowired
    private EntityBuilder<DinnerCart, String> entityBuilder;

    @Resource
    private DinnerCartItemRepository dinnerCartItemRepository;

    @Transactional
    public DinnerCart saveOrUpdateCart(String request) throws Exception {
        DinnerCart dinnerCart = entityBuilder.build(request);
        if (dinnerCart.getId() == null) {
            /* If a cart exist for this buyer then set the cart to expired */
            List<DinnerCart> dinnerCarts = dinnerCartRepository.cartByBuyer(dinnerCart.getBuyer().getId());
            if (dinnerCarts != null && !dinnerCarts.isEmpty()) {
                dinnerCarts.stream().filter(d -> d.getStatus() == OrderStatus.ORDER_IN_PROGRESS).forEach(d -> d.setStatus(OrderStatus.EXPIRED));
            }
            dinnerCartRepository.save(dinnerCarts);
        }
        DinnerCart dc = dinnerCartRepository.save(dinnerCart);
        dinnerCart.getCartItems().stream().forEach(item -> item.setDinnerCart(dc));
        dinnerCartItemRepository.save(dinnerCart.getCartItems());
        dc.setCartItems(dinnerCart.getCartItems());
        return dinnerCartRepository.save(dc);
    }

    @Transactional
    public DinnerCart saveOrUpdateCart(DinnerCart dinnerCart) throws Exception {
        return dinnerCartRepository.save(dinnerCart);
    }

    public Integer getCartItemCount(Integer cartId) {
        return dinnerCartItemRepository.getCartItemCount(cartId);
    }

    @Transactional
    public Collection<DinnerCartItem> getCartItems(Integer cartId) {
        DinnerCart cart = dinnerCartRepository.findOne(cartId);
        return cart.getCartItems();
    }

    @Transactional
    public DinnerCart getCart(Integer cartId) throws Exception {
        DinnerCart cart = dinnerCartRepository.findOne(cartId);
        return cart;
    }

    @Transactional
    public DinnerCart getCartByProfileId(Integer profileId) throws Exception {
        Page<DinnerCart> cart = dinnerCartRepository.findByProfileId(profileId, new PageRequest(0, 1));
        if (cart.getSize() != 0 && cart.getContent() != null && !cart.getContent().isEmpty()) {
            DinnerCart c = cart.getContent().get(0);
            Calendar currentTime = Calendar.getInstance();
			/* Cart is treated as expired if it's been create 30 minutes back */
            if (currentTime.getTimeInMillis() - c.getCreatedDate().getTimeInMillis() < 30 * 60 * 1000 && c.getStatus().equals(OrderStatus.ORDER_IN_PROGRESS)) {
                return c;
            }

        }
        return null;
    }

    @Transactional
    public void placeOrder(Integer profileId, Integer cartId, OrderStatus status) throws Exception {
        LOGGER.info("Finding cart profileId => {}, cartId => {}", profileId, cartId);
        DinnerCart cart = dinnerCartRepository.findOne(cartId);
        if (cart == null) {
            throw new Exception("Invalid cart id or order might have expired");
        }
        if (cart.getStatus() == OrderStatus.RECEIVED) {
            cart.setModifiedDate(Calendar.getInstance());
            String passCode = RandomStringUtils.randomAlphanumeric(8);
            cart.setPassCode(passCode);
            cart.setModifiedDate(Calendar.getInstance());
            cart.setStatus(OrderStatus.RECEIVED);
            dinnerCartRepository.save(cart);
        }
    }

    @Transactional
    public void update(DinnerCart cart) throws Exception {
        LOGGER.info("Finding cart with cart id {}", cart.getId());
        DinnerCart dinnerCart = dinnerCartRepository.findOne(cart.getId());
        if (cart == null) {
            throw new Exception("Invalid cart id");
        }
        dinnerCart.setBuyerRating(cart.getBuyerRating());
        dinnerCart.setSellerRating(cart.getSellerRating());
        dinnerCartRepository.save(dinnerCart);
    }

    public int countOrderedQuantityByListingId(Integer listingId) {
        Integer rowCount = dinnerCartItemRepository.countOrderQuantityByListingId(listingId);
        return rowCount == null ? 0 : rowCount.intValue();
    }

    public List<Object[]> getReceivedOrderBySellerProfile(int profileId) {
        return dinnerCartRepository.cartBySeller(profileId);
    }

    public List<Object[]> receivedOrdersByDate(Calendar startDate, Calendar endDate) {
        return dinnerCartRepository.findOrdersByDate(startDate, endDate);
    }

    public List<Object[]> receivedOrdersByDateAndTier(Calendar startDate, Calendar endDate, String tier) {
        return dinnerCartRepository.findOrdersByDateAndTier(startDate, endDate, tier);
    }

    public List<Object[]> receivedOrdersByProfileAndDate(Integer profileId, Calendar startDate, Calendar endDate) {
        return dinnerCartRepository.findOrdersByProfileAndDate(profileId, startDate, endDate);
    }

    public Collection<DinnerCart> orderHistory(Integer profileId) {
        PageRequest request = new PageRequest(0, 10, Sort.Direction.DESC, "createdDate");
        return dinnerCartRepository.orderHistory(profileId, request);
    }

    public List<OrderedItemDetailVO> recievedOrderItemDetail(Integer listingId) {
        return dinnerCartRepository.findReceivedOrderItemDetail(listingId);
    }

    public List<OrderedItemDetailVO> receivedOrdersByBuyerProfileAndPassCode(Integer profileId, String passCode) {
        return dinnerCartRepository.findReceivedOrderItemDetailByBuyerAndPassCode(profileId, passCode);
    }

    public List<OrderedItemDetailVO> receivedOrdersByBuyerProfileAndDate(Integer profileId, DateTime startDate, DateTime endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate.toDateTime(DateTimeZone.UTC).toDate());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate.toDateTime(DateTimeZone.UTC).toDate());
        return dinnerCartRepository.findReceivedOrderItemDetailByBuyerAndDate(profileId, startDate.toDateTime(DateTimeZone.UTC).toDate(), endDate.toDateTime(DateTimeZone.UTC).toDate());
    }

    public Double avgSellerRating(Integer profileId) {
        return dinnerCartRepository.getAvgSellerRating(profileId);
    }

}
