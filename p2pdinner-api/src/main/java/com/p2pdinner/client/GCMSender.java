package com.p2pdinner.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class GCMSender implements InitializingBean {


    private static final Logger LOGGER = LoggerFactory.getLogger(GCMSender.class);

    private String gcmUrl;
    private String gcmApiKey;


    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(String deviceToken, String confirmationCode, Double price) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(gcmUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "key=" + gcmApiKey);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("to", deviceToken);
        Map<String, Object> notificationData = new HashMap<String, Object>();
        notificationData.put("title", "Order Summary");
        notificationData.put("price", decimalFormat.format(price));
        notificationData.put("confirmationCode", confirmationCode);
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


}