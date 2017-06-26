package com.p2pdinner.utils;

import java.security.MessageDigest;

public class CommonUtils {
	public static String getHashValue(String password) throws Exception {
    	MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = password.getBytes();
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }
}
