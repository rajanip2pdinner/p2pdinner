package com.p2p.common.notifications;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.p2p.common.s3.StorageOperations;

@Component("apple")
public class ApplePushNotification implements PushNotification, InitializingBean, DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushNotification.class);
	private Path certificatePath = null;

	@Autowired
	private StorageOperations storageOperations;

	private ApnsService service;


	private void sendNotification(String deviceToken, NotificationMessage notificationMessage) throws Exception {
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

	@Override
	public void afterPropertiesSet() throws Exception {
		certificatePath = storageOperations.readObject("certificates/Push.p12");
		service = APNS.newService().withCert(certificatePath.toFile().getAbsolutePath(), "password1")
				.withSandboxDestination().build();
	}

	@Override
	public void destroy() throws Exception {
		if (certificatePath != null) {
			Files.deleteIfExists(certificatePath);
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
