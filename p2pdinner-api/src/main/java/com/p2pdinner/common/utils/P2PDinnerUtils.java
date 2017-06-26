package com.p2pdinner.common.utils;

import org.apache.commons.lang3.StringUtils;

public class P2PDinnerUtils {
	private static final String[] IMAGE_EXTNS = { ".png", ".jpg", ".gif", ".bmp", "jpeg" };
	public static Boolean isValidImageExtn(String filename) {
		Boolean validExtn = Boolean.FALSE;
		for(String imageExtn : IMAGE_EXTNS) {
			if (!validExtn && filename.toLowerCase().endsWith(imageExtn)) {
				validExtn = Boolean.TRUE;
			}
		}
		return validExtn;
	}
	
	public static String fileExtn(String filename) {
		if (StringUtils.isNotEmpty(filename) && filename.contains(".")) {
			String[] fileParts = filename.split("\\.");
			return fileParts[fileParts.length - 1];
		}
		return StringUtils.EMPTY;
	}
}
