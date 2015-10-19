package com.moon.appframework.common.router;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kidcrazequ
 *
 */
public class UrlParser {

    /**
     * 解析url query参数
     * @param query query参数
     * @return Map.如{scanType=sao, isActivity=true, activityId=11, tips=中国国歌}
     */
    public Map<String, Object> parse(String query){
        Map<String, Object> query_pairs = new HashMap<String, Object>();
        try {
            query_pairs = splitQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query_pairs;
    }

    /**
     * query转换map
     * @param query
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, Object> splitQuery(String query) throws UnsupportedEncodingException {
        final Map<String, Object> query_pairs = new HashMap<String, Object>();
        final String[] pairs = query.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            final Object value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.put(key, value);
        }
        return query_pairs;
    }


}
