package com.p2p.common.config;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;
import com.p2p.common.messagebuilders.ExceptionMessageBuilder;
import com.p2p.common.notifications.ApplePushNotification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PCommonContextConfig.class)
public class P2PCommonContextConfigTest {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ExceptionMessageBuilder excepBuilder;
	
	@Autowired
	private Environment environment;
	
	@Test
	public void testMessageSource() {
		String message = messageSource.getMessage("p2pdinner.invalid_profile_id", new Object[] {"50"}, Locale.US);
		System.out.println(message);
		Assert.assertNotNull(message);
	}
	
	@Test
	public void testMessageSourceNewMessage() {
		P2PDinnerException excep = excepBuilder.createException(ErrorCode.ADD_MI_FAILED, new Object[] {"test"}, Locale.US);
		Assert.assertNotNull(excep);
		System.out.println(excep.getMessage());
	}
	
}
