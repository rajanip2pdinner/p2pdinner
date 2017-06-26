package com.p2pdinner.common.notifications;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Map;

@Component("apple")
public class ApplePushNotification implements PushNotification {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushNotification.class);

	@Autowired
	private ApnsService service;

	private void sendBuyerNotification(String deviceToken, NotificationMessage notificationMessage) throws Exception {
		LOGGER.info("Sending notification to apple device with token : {}", deviceToken);
		DecimalFormat decimalFormat = new DecimalFormat("00.00");
		StringBuffer message = new StringBuffer();
		message.append("Order placed successfully with confirmation code - ")
				.append(notificationMessage.getConfirmationCode())
				.append(", Price: ")
				.append(decimalFormat.format(notificationMessage.getPrice()));
		String payload = APNS.newPayload().alertBody(message.toString())
				.build();

		service.push(deviceToken, payload);
	}

	private void sendSellerNotification(String deviceToken, NotificationMessage notificationMessage) throws Exception {
		LOGGER.info("Sending notification to apple device with token : {}", deviceToken);
		DecimalFormat decimalFormat = new DecimalFormat("00.00");
		StringBuffer message = new StringBuffer();
		message.append(notificationMessage.getOrderQuantity())
				.append(" " + notificationMessage.getTitle() + " order(s) received with buyer confirmation code :")
				.append(notificationMessage.getConfirmationCode())
				.append(", Price: ")
				.append(decimalFormat.format(notificationMessage.getPrice()));
		String payload = APNS.newPayload().alertBody(message.toString())
				.build();

		service.push(deviceToken, payload);
	}

	private void sendNotification(String deviceToken, NotificationMessage notificationMessage) throws Exception {
		LOGGER.info("Sending notification to apple device with token : {}", deviceToken);
		switch (notificationMessage.getNotificationTarget()) {
			case BUYER:
				sendBuyerNotification(deviceToken, notificationMessage);
				break;
			case SELLER:
				sendSellerNotification(deviceToken, notificationMessage);
				break;
		}
	}

	@Override
	public void sendNotification(Map<NotificationToken, Object> attributes, NotificationMessage notificationMessage) throws Exception {
		if (attributes != null && !attributes.isEmpty() && attributes.containsKey(NotificationToken.DEVICE_TOKEN)) {
			sendNotification(attributes.get(NotificationToken.DEVICE_TOKEN).toString(), notificationMessage);
		} else {
			throw new Exception("Device Token missing. Failed to send notification");
		}

	}
}
