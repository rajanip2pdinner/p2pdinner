package com.p2pdinner.common.codes;

public enum ErrorCode {
	
	INVALID_PROFILE("p2pdinner.invalid_profile_id"),
	BAD_REQUEST("p2pdinner.bad_request"),
	ADD_MI_FAILED("p2pdinner.add_menuitem_failed"),
	UNKNOWN("p2pdinner.unknown"),
	UPLOAD_ERROR("p2pdinner.upload_error"), 
	INVALID_FILE_EXTN("p2pdinner.invalid_file_extn"),
	ADD_CART_FAILED("p2pdinner.add_cart_failed"),
	NO_LISTINGS("p2pdinner.no_listings"),
	UNAUTHORIZED("p2pdinner.user_not_found"),
	INVALID_MENU_ITEM("p2pdinner.update_menuitem_failed"),
	ADD_TO_LIST_INVALID_DATES("p2pdinner.add_to_list_invalid_dates"),
	MISSING_TITLE("p2pdinner.missing.title"),
	INVALID_CATEGORY("p2pdinner.invalid.category"),
	MISSING_ORDER_DATE("p2pdinner.missing.orderdate"),
	INVALID_START_TIME("p2pdinner.invalid.starttime"),
	INVALID_CLOSE_TIME("p2pdinner.invalid.closetime"),
	INVALID_ADDRESS("p2pdinner.invalid.address"),
	REQUIRED_FIELD_MISSING("p2pdinner.missing_reqd_fields");


	
	String errorCode;
	
	ErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}
}
