package com.p2p.common.notifications;

import java.util.Map;

public interface PushNotification {
	public void sendNotification(Map<NotificationToken, Object> attributes, NotificationMessage notificationMessage) throws Exception;
}
