package com.p2pdinner.common.messagebuilders;

import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionMessageBuilder {
	@Autowired
	private MessageSource messageSource;
	
	public P2PDinnerException createException(ErrorCode errorCode, Object[] args, Locale locale) {
		String eCode = errorCode.getErrorCode();
		String eMessage = messageSource.getMessage(errorCode.getErrorCode(), args, locale);
		return new P2PDinnerException(eCode, eMessage);
	}
	
	public P2PDinnerException createException(int httpStatus, ErrorCode errorCode, Object[] args, Locale locale) {
		String eCode = errorCode.getErrorCode();
		String eMessage = messageSource.getMessage(errorCode.getErrorCode(), args, locale);
		return new P2PDinnerException(httpStatus, eCode, eMessage);
	}
}
