package com.moon.myreadapp.common.components.rss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenhaohui on 16/6/22.
 */
public class RssHelper {
    public static boolean isURL(String str) {
        if (str == null) return false;
        //转换为小写
        str = str.replace("  ", "");
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String adapterURL(String url) {
        String urls = url.replace(" ", "").replace("\n|\r", "").toLowerCase();
        if (!urls.startsWith("http://") && !urls.startsWith("https://")) {
            urls = "http://" + urls;
        }
        return urls;
    }

}
