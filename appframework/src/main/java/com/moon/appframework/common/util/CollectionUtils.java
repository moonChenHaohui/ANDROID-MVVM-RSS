package com.moon.appframework.common.util;

import java.util.List;
import java.util.Map;

public class CollectionUtils {
	
	public static <T> boolean isEmptyList(final List<T> list) {
        return list == null || list.isEmpty();
    }
	
	public static <T> boolean isNotEmptyList(final List<T> list) {
        return !isEmptyList(list);
    }
	
	public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }
	
	public static boolean isNotEmpty(final Map<?,?> map) {
        return !isEmpty(map);
    }

}
