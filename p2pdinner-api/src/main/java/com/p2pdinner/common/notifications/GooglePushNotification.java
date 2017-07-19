package com.p2pdinner.common.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


@Component("android")
public class GooglePushNotification implements PushNotification, InitializingBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GooglePushNotification.class);

    private String gcmUrl;
    private String gcmApiKey;


    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    private void sendNotification(String deviceToken, NotificationMessage notificationMessage) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(gcmUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "key=" + gcmApiKey);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("to", deviceToken);
        Map<String, Object> notificationData = new HashMap<String, Object>();
        notificationData.put("title", "Order Summary");
        notificationData.put("price", decimalFormat.format(notificationMessage.getPrice()));
        notificationData.put("confirmationCode", notificationMessage.getConfirmationCode());
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTime startDate = new DateTime(notificationMessage.getStartDate());
        DateTime endDate = new DateTime(notificationMessage.getEndDate());
        notificationData.put("startTime", dateTimeFormatter.print(startDate));
        notificationData.put("endTime", dateTimeFormatter.print(endDate));
        notificationData.put("address", notificationMessage.getAddress());
        notificationData.put("target", notificationMessage.getNotificationTarget().name());
        notificationData.put("orderQuantity", notificationMessage.getOrderQuantity());
        notificationData.put("menuItemTitle", notificationMessage.getTitle());
        requestBody.put("data", notificationData);
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(requestBody), httpHeaders);
        UriComponents uriComponents = uriComponentsBuilder.build();
        String response = restTemplate.postForObject(uriComponents.toUri(), requestEntity, String.class);
        LOGGER.info("Received response {}", response);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        gcmUrl = env.getProperty("gcm.url");
        gcmApiKey = env.getProperty("gcm.api.key");
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
