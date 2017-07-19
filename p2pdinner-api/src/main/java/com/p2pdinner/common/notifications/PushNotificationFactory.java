package com.p2pdinner.common.notifications;

public interface PushNotificationFactory {
	PushNotification getNotificationByDeviceType(String deviceType);
}
