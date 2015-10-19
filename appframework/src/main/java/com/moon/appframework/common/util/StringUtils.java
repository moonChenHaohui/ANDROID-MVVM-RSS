package com.moon.appframework.common.util;

/**
 * @author kidcrazequ
 *
 */
public class StringUtils {
	
	public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
	
	public static boolean isNotEmpty(final CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }
	
	public static boolean isNull(String s){
		return s==null || "null".equals(s);
	}
	
	public static String nullToString(String s){
		return isNull(s)?"":s;
	}

}
