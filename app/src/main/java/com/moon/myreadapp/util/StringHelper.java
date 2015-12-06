package com.moon.myreadapp.util;

import com.moon.appframework.common.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by moon on 15/12/6.
 */
public class StringHelper {

    public static String SPLIT = ",";

    public static ArrayList<String> convertStringToList(String images){
        if (StringUtils.isEmpty(images)){
            return null;
        }
        ArrayList<String> re = new ArrayList<>();
        String[] result = images.split(SPLIT);
        for (String s : result){
            re.add(s);
        }
        result = null;
        return re;
    }

    public static String convertListToSrring(ArrayList<String> strings){
        if (strings == null || strings.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        int len = strings.size();
        for (int i = 0;i < len;i++){
            sb.append(strings.get(i));
            if (i < len - 1){
                sb.append(SPLIT);
            }
        }
        return sb.toString();
    }
}
