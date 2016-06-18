package com.p2p.common.notifications;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.p2p.common.config.P2PCommonContextConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PCommonContextConfig.class)
public class EmailNotificationTest {

	@Autowired
	private EmailNotification notification;

	@Test
	public void sendNotification() throws Exception {
		Map<NotificationToken,Object> attributes = new HashMap<NotificationToken, Object>();
		attributes.put(NotificationToken.TO, new String[] { "rajani@p2pdinner.com", "rajanikanthy@outlook.com" });
		attributes.put(NotificationToken.FROM, "rajani@p2pdinner.com");
		notification.sendNotification(attributes, new NotificationMessage());

	}
	
}
