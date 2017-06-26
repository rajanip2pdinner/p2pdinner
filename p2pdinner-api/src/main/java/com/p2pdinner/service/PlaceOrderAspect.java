package com.p2pdinner.service;

import com.p2pdinner.common.notifications.*;
import com.p2pdinner.repositories.DinnerCartRepository;
import com.p2pdinner.domain.DinnerCart;
import com.p2pdinner.domain.DinnerCartItem;
import com.p2pdinner.domain.OrderStatus;
import com.p2pdinner.domain.UserProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
public class PlaceOrderAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceOrderAspect.class);

    @Autowired
    private StripeDataService stripeDataService;

    @Resource
    private DinnerCartRepository dinnerCartRepository;

    @Autowired
    @Qualifier("pushNotificationFactory")
    private PushNotificationFactory pushNotificationFactory;

    @Autowired
    private UserProfileDataService userProfileDataService;

    @Pointcut("execution(* com.p2pdinner.service.DinnerCartDataService.placeOrder(Integer, Integer, com.p2pdinner.domain.OrderStatus)) && args(profileId, cartId, status)")
    public void placeOrder(Integer profileId, Integer cartId, com.p2pdinner.domain.OrderStatus status) {
    }

    @Before("placeOrder(profileId,cartId,status)")
    @Transactional
    public void chargeCustomer(Integer profileId, Integer cartId, OrderStatus status) throws Exception {
        LOGGER.info("-----BEFORE------");
        DinnerCart cart = dinnerCartRepository.findOne(cartId);
        chargeCustomer(cart);
        dinnerCartRepository.save(cart);
    }

    public void chargeCustomer(DinnerCart dinnerCart) throws Exception {
        LOGGER.info("Charging customer for items...");
        try {
            /*if (StringUtils.isNotEmpty(dinnerCart.getBuyer().getStripeCustomerId())) {
				Double totalPrice =  0d;
				for(DinnerCartItem item : dinnerCart.getCartItems()) {
					totalPrice += item.getTotalPrice();
				}
				//String chargeId = stripeDataService.charge(dinnerCart.getBuyer(), dinnerCart.getId(), dinnerCart.getPassCode(), totalPrice);
				//dinnerCart.setChargeId(chargeId);
				String chargeId = RandomStringUtils.random(20);
				dinnerCart.setChargeId(chargeId);
			} else {
				dinnerCart.setStatus(OrderStatus.MISSING_PAYMENT_INFORMATION);
				throw new P2PDinnerDataException(Reason.NO_PAYMENT_INFORMATION, "No payment information for buyer " + dinnerCart.getBuyer().getEmailAddress());
			}*/
            String chargeId = RandomStringUtils.random(20);
            dinnerCart.setChargeId(chargeId);
            dinnerCart.setStatus(OrderStatus.RECEIVED);
        } /*catch (P2PDinnerDataException pExcep) {
			dinnerCart.setStatus(OrderStatus.MISSING_PAYMENT_INFORMATION);
			dinnerCartRepository.save(dinnerCart);
			throw pExcep;
		}*/ catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            dinnerCart.setStatus(OrderStatus.PROCESSING_ERROR);
            dinnerCartRepository.save(dinnerCart);
            throw ex;
        }
    }

    @After("placeOrder(profileId,cartId,status)")
    @Transactional
    public void updateAvailability(Integer profileId, Integer cartId, OrderStatus status) throws Exception {
        LOGGER.info("-----AFTER------");
        LOGGER.info("Updating available quantity....");
        DinnerCart cart = dinnerCartRepository.getOne(cartId);
        Set<UserProfile> sellerNotificationProfiles = new HashSet<>();
        if (cart.getStatus() == OrderStatus.RECEIVED) {
            for (DinnerCartItem cartItem : cart.getCartItems()) {
                int orderQuantity = cartItem.getOrderQuantity();
                cartItem.getDinnerListing().setAvailableQuantity(cartItem.getDinnerListing().getAvailableQuantity() - orderQuantity);
                cartItem.getDinnerListing().setOrderQuantity(cartItem.getDinnerListing().getOrderQuantity() + orderQuantity);
                sellerNotificationProfiles.add(cartItem.getDinnerListing().getMenuItem().getUserProfile());
            }
        }
        dinnerCartRepository.save(cart);
        UserProfile buyerProfile = userProfileDataService.getProfileById(profileId);
        prepareAndSendNotification(NotificationTarget.BUYER, cart, buyerProfile);
        sellerNotificationProfiles.stream().forEach(notificationProfile -> {
            prepareAndSendNotification(NotificationTarget.SELLER, cart, notificationProfile);
        });

    }

    private void prepareAndSendNotification(NotificationTarget notificationTarget, DinnerCart cart, UserProfile notificationProfile) {
        if (notificationProfile != null && notificationProfile.getDevices() != null && !notificationProfile.getDevices().isEmpty()) {
            Map<NotificationToken, Object> arguments = new HashMap<>();
            //prepareNotificationMessages
            List<NotificationMessage> notificationMessages = prepareNotifications(notificationTarget, cart.getPassCode(), cart.getCartItems());
            notificationMessages.forEach(notificationMessage -> {
                notificationProfile.getDevices().forEach(device -> {
                    PushNotification notification = pushNotificationFactory.getNotificationByDeviceType(device.getDeviceType().toLowerCase());
                    if (notification == null) {
                        notification = pushNotificationFactory.getNotificationByDeviceType("other");
                    }
                    try {
                        arguments.put(NotificationToken.DEVICE_TOKEN, device.getRegistrationId());
                        notification.sendNotification(arguments, notificationMessage);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            });
        }
    }

    private List<NotificationMessage> prepareNotifications(NotificationTarget notificationTarget, String confirmationCode, Set<DinnerCartItem> dinnerCartItems) {
        Map<String, List<DinnerCartItem>> values = dinnerCartItems.stream().collect(Collectors.groupingBy(ci -> ci.getDinnerListing().getAddressLine1() + "," + ci.getDinnerListing().getAddressLine2() + "," + ci.getDinnerListing().getCity() + ci.getDinnerListing().getState()));
        List<NotificationMessage> notificationMessages = new ArrayList<>();
        values.keySet()
                .forEach(address -> {
                    NotificationMessage notificationMessage = new NotificationMessage();
                    notificationMessage.setNotificationTarget(notificationTarget);
                    Double totalPrice = values.get(address).parallelStream().mapToDouble(ci -> ci.getTotalPrice()).sum();
                    notificationMessage.setAddress(address);
                    notificationMessage.setConfirmationCode(confirmationCode);
                    notificationMessage.setPrice(totalPrice);
                    notificationMessage.setStartDate(values.get(address).get(0).getDinnerListing().getStartTime());
                    notificationMessage.setEndDate(values.get(address).get(0).getDinnerListing().getEndTime());
                    notificationMessage.setOrderQuantity(values.get(address).get(0).getOrderQuantity());
                    notificationMessage.setTitle(values.get(address).get(0).getDinnerListing().getMenuItem().getTitle());
                    notificationMessages.add(notificationMessage);
                });
        return notificationMessages;
    }

    @AfterThrowing("placeOrder(profileId,cartId,status)")
    @Transactional
    public void refundCustomer(Integer profileId, Integer cartId, OrderStatus status) throws Exception {
        LOGGER.info("----- AFTER THROWING------");
        LOGGER.info("refund customer....");
        DinnerCart cart = dinnerCartRepository.getOne(cartId);
        if (cart.getStatus() == OrderStatus.RECEIVED && StringUtils.isNotEmpty(cart.getChargeId())) {
            cart.setStatus(OrderStatus.REFUND);
            cart.setChargeId(stripeDataService.refund(cart.getChargeId()));
            //RESET quantity availability limit
            for (DinnerCartItem cartItem : cart.getCartItems()) {
                int orderQuantity = cartItem.getOrderQuantity();
                cartItem.getDinnerListing().setAvailableQuantity(cartItem.getDinnerListing().getAvailableQuantity() + orderQuantity);
                cartItem.getDinnerListing().setOrderQuantity(cartItem.getDinnerListing().getOrderQuantity() - orderQuantity);
            }
            dinnerCartRepository.save(cart);
        }
    }

}
