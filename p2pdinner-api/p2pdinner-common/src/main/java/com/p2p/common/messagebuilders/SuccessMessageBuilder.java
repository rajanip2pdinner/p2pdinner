package com.p2p.common.messagebuilders;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.p2p.common.codes.SuccessCode;

@Component
public class SuccessMessageBuilder {
	@Autowired
	private MessageSource messageSource;
	
	public SuccessMessage createSuccessMessage(SuccessCode successCode, Object[] args, Locale locale, Map<String,Object> data) {
		String sCode = successCode.getSuccessCode();
		String sMessage = messageSource.getMessage(successCode.getSuccessCode(), args, locale);
		SuccessMessage successMessage = new SuccessMessage();
		successMessage.setData(data);
		successMessage.setMessage(sMessage);
		successMessage.setStatus(sCode);
		return successMessage;
	}
}
