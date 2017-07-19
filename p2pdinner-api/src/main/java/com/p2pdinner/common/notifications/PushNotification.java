package com.p2pdinner.common.notifications;

import java.util.Map;

public interface PushNotification {
	public void sendNotification(Map<NotificationToken, Object> attributes, NotificationMessage notificationMessage) throws Exception;
}
