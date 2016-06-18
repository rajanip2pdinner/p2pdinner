package com.p2p.common.notifications;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.p2p.common.config.P2PCommonContextConfig;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PCommonContextConfig.class)
public class PushNotificationFactoryTest {

	@Autowired
	private PushNotificationFactory pushNotificationFactory;

	@Test
	public void testAppleNotification() throws Exception {
		Map<NotificationToken, Object> notificationTokenObjectMap = new HashMap<>();
		notificationTokenObjectMap.put(NotificationToken.DEVICE_TOKEN, "eae0681497d023e1690a93032e01feb5d4c93c7bd8509eaa7edd49824e1f243a");
		pushNotificationFactory.getNotificationByDeviceType("apple")
				.sendNotification(notificationTokenObjectMap, new NotificationMessage());
	}

	@Test
	@Ignore
	public void testGoogleNotification() throws Exception {
		Map<NotificationToken, Object> notificationTokenObjectMap = new HashMap<>();
		notificationTokenObjectMap.put(NotificationToken.DEVICE_TOKEN, "eae0681497d023e1690a93032e01feb5d4c93c7bd8509eaa7edd49824e1f243a");
		pushNotificationFactory.getNotificationByDeviceType("android")
				.sendNotification(notificationTokenObjectMap,new NotificationMessage());
	}

}
