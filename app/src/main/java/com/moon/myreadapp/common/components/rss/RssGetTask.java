package com.moon.myreadapp.common.components.rss;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.module.DCModule;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;

import java.util.List;

/**
 * Created by moon on 15/10/29.
 */
public class RssGetTask extends AsyncTask<String, Integer, SyndFeed>
{
    /**
     *
     */
    private SyndFeed feed;
    private Activity context;

    public RssGetTask (){
    }


    private void l (String str){
        if (null != str){
            XLog.e(str);
        }
    }

    @Override
    protected SyndFeed doInBackground(String... params)
    {
        if (params == null || params.length == 0){
            return null;
        }
        String url = params[0];
        RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();
        feed = rssAtomFeedRetriever.getMostRecentNews(url);


        l(feed.getFeedType());//rss 类型
        l(feed.getDescription());//频道说明
        l(feed.getLink());//频道链接
        feed.getModules();//什么模块,,等待查看
        l(feed.getTitle());//频道标题
        feed.getForeignMarkup();//这个是什么
        List<DCModule> modules = feed.getModules();
        for (DCModule module : modules) {
            l(module.getDate().toString());//发布时间
            l(module.getLanguage());//语言
            l(module.getRights());//版权所有
            l(module.getUri());//uri
            l(module.getCreator());//作者

        }

        List<SyndEntry> list = feed.getEntries();
        for (SyndEntry entry : list) {
            l(entry.getLink());//feed链接
            l(entry.getUri());//uri
            SyndContent content = entry.getDescription();
            l(content.getType());//内容编码类型
            l(content.getValue());//html编码的内容
            List<DCModule> entryModules = entry.getModules();
            for (DCModule module : entryModules) {
                l(module.getDate().toString());//发布时间
                l(module.getLanguage());//语言
                l(module.getRights());//版权所有
                l(module.getUri());//uri
                l(module.getCreator());//作者

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(SyndFeed syndFeed)
    {
        super.onPostExecute(syndFeed);

        // setContentView(createList(feed, getContext()));
    }


}
