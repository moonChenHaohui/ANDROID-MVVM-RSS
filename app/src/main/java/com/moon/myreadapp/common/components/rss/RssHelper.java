package com.moon.myreadapp.common.components.rss;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.module.DCModule;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.mvvm.models.dao.Feed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by moon on 15/11/11.
 */
public class RssHelper {

    private static RssAtomFeedRetriever retriever;

    public static RssAtomFeedRetriever getRetriever() {
        if (null == retriever) {
            retriever = new RssAtomFeedRetriever();
        }
        return retriever;
    }


    public static synchronized void getMostRecentNews(final String feedUrl, IRssListener listener) {
        if (listener == null) return;

        //TODO 这里使用threadtool or RxAndroid?
        new RssTask(listener).execute(feedUrl);
    }


    /**
     * 不能直接进行ui更新
     */
    public interface IRssListener {

        void onSuccess(SyndFeed syndFeed);

        void onError(String msg);
    }

    public static class RssTask extends AsyncTask<String, Integer, SyndFeed> {
        private SyndFeed feed;
        private IRssListener listener;

        String url;


        public RssTask(IRssListener listener) {
            this.listener = listener;
        }


        @Override
        protected SyndFeed doInBackground(String... params) {
            url = params[0];
            if (!isURL(params[0])) {
                listener.onError("url 错误");
                return null;
            }
            url = adapterURL(url);
            XLog.d(url);
            try {
                feed = getRetriever().retrieveFeed(url);
            } catch (Exception e) {
                String ex = e.toString();
                if (ex != null && ex != "") {
                    Log.d("getMostRecentNews", ex);
                    String[] array = ex.split("[\\D]+");
                    if (array == null || array.length == 0) {
                        listener.onError("网络连接出错.");
                        return null;
                    }
                    String code = array[array.length - 1];
                    int codeNumber = 404;
                    try {
                        codeNumber = Integer.valueOf(code);
                    } catch (Exception e1) {

                    }

                    listener.onError("错误代码:" + codeNumber);
                    return null;
                }
            }

            listener.onSuccess(feed);
            return null;
        }
    }

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

//    l(feed.getFeedType());//rss 类型
//    l(feed.getDescription());//频道说明
//    l(feed.getLink());//频道链接
//    feed.getModules();//什么模块,,等待查看
//    l(feed.getTitle());//频道标题
//    feed.getForeignMarkup();//这个是什么
//    List<DCModule> modules = feed.getModules();
//    for (DCModule module : modules) {
//        l(module.getDate().toString());//发布时间
//        l(module.getLanguage());//语言
//        l(module.getRights());//版权所有
//        l(module.getUri());//uri
//        l(module.getCreator());//作者
//
//    }
//
//    List<SyndEntry> list = feed.getEntries();
//    for (SyndEntry entry : list) {
//        l(entry.getLink());//feed链接
//        l(entry.getUri());//uri
//        SyndContent content = entry.getDescription();
//        l(content.getType());//内容编码类型
//        l(content.getValue());//html编码的内容
//        List<DCModule> entryModules = entry.getModules();
//        for (DCModule module : entryModules) {
//            l(module.getDate().toString());//发布时间
//            l(module.getLanguage());//语言
//            l(module.getRights());//版权所有
//            l(module.getUri());//uri
//            l(module.getCreator());//作者
//
//        }
//    }
}
