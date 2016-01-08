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
            String iconUrl = url.getProtocol() + "://" + url.getHost() + "/favicon.ico";// 保证从域名根路径搜索
            return iconUrl;
        }catch (MalformedURLException e){
            return null;
        }
    }


    /**
     * 从本地处理html文本
     * @param url
     * @return
     */
    public static Spanned getHtmlByRes(String url){
        final Html.ImageGetter imageGetter = new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                int rId=Integer.parseInt(source);
                drawable=Globals.getApplication().getResources().getDrawable(rId);
                int size =Globals.getApplication().getResources().getDimensionPixelOffset(R.dimen.text_h2);
                drawable.setBounds(0, 0,size, size);
                return drawable;
            }
        };
        return Html.fromHtml(url, imageGetter, null);
    }

}
