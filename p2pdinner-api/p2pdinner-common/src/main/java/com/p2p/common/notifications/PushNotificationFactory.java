package com.p2p.common.notifications;

public interface PushNotificationFactory {
	PushNotification getNotificationByDeviceType(String deviceType);
}
