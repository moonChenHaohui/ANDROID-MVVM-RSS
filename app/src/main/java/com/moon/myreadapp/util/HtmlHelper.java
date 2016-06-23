package com.moon.myreadapp.util;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import com.moon.myreadapp.R;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by moon on 15/11/29.
 */
public class HtmlHelper {


    /**
     * 得到网页中图片的地址
     */
    public static ArrayList<String> getImgStr(String htmlStr,final int maxSize) {
        ArrayList<String> result = new ArrayList<>();
        String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern pImage = Pattern.compile(imgRegex);
        Matcher mImage = pImage.matcher(htmlStr);
        try{
            while (mImage.find()) {
                String imagePath = mImage.group(1);
                if (result.size() >= maxSize){
                    return result;
                }
                result.add(imagePath);
            }
        }catch (Exception e){
            return result;
        }
        return result;
    }


    /**
     * 拿到网站icon
     * @param urlString
     * @return
     */
    public static String getIconUrlString( String urlString ) {
        if (urlString == null || urlString.isEmpty()){
            return null;
        }
        try{
            URL url = new URL( urlString );
            String iconUrl = "http://statics.dnspod.cn/proxy_favicon/_/favicon?domain=" + url.getHost();// 使用dnspod进行favicon的获取
            return iconUrl;
        }catch (MalformedURLException e){
            return null;
        }
    }


}
