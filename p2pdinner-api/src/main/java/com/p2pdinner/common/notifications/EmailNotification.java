package com.p2pdinner.common.notifications;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component("other")
public class EmailNotification implements InitializingBean, PushNotification {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotification.class);

	@Autowired
	private Environment environment;
	
	private String sendGridUserName;
	private String sendGridPassword;
	
	private void sendNotification(String[] to, String from, String subjectTemplate, String bodyTemplate) {
		SendGrid sendgrid = new SendGrid(this.sendGridUserName, this.sendGridPassword);
		SendGrid.Email email = new SendGrid.Email();
		email.addTo(to);
		if (!StringUtils.isEmpty(subjectTemplate)) {
			email.setSubject(subjectTemplate);
		}
		email.setHtml(bodyTemplate);
		email.setFrom(from);
		try {
			SendGrid.Response response = sendgrid.send(email);
			System.out.println(response.getMessage());
		} catch (SendGridException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}


	public void afterPropertiesSet() throws Exception {
		this.sendGridPassword = environment.getProperty("sendgrid.password");
		this.sendGridUserName = environment.getProperty("sendgrid.username");
	}

	@Override
	public void sendNotification(Map<NotificationToken, Object> attributes, NotificationMessage notificationMessage) throws Exception {
		if (attributes != null && !attributes.isEmpty() && attributes.containsKey(NotificationToken.TO)) {
			String[] to = (String[]) attributes.get(NotificationToken.TO);
			String from = attributes.get(NotificationToken.FROM).toString();
			String subject = "Order Confirmation - " + notificationMessage.getConfirmationCode();
			String body = "Your order received with confirmation code - " + notificationMessage.getConfirmationCode() + ", price - " + notificationMessage.getPrice();
			this.sendNotification(to, from, subject, body);
		} else {
			throw new Exception("Device Token missing. Failed to send notification");
		}

	}
	
	
}
