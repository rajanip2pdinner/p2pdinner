package com.p2pdinner.common.messagebuilders;

import com.p2pdinner.common.codes.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

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
