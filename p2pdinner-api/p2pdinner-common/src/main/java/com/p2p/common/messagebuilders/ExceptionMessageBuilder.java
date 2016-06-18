package com.p2p.common.messagebuilders;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.p2p.common.codes.ErrorCode;
import com.p2p.common.exceptions.P2PDinnerException;

@Component
public class ExceptionMessageBuilder {
	@Autowired
	private MessageSource messageSource;
	
	public P2PDinnerException createException(ErrorCode errorCode, Object[] args, Locale locale) {
		String eCode = errorCode.getErrorCode();
		String eMessage = messageSource.getMessage(errorCode.getErrorCode(), args, locale);
		return new P2PDinnerException(eCode, eMessage);
	}
}
