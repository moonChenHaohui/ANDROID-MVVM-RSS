package com.moon.myreadapp.common.components.rss;

import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URL;

public class RssAtomFeedRetriever
{


    private static FeedFetcher feedFetcher;

    private FeedFetcher getFeedFetcher (){
        if (null == feedFetcher){
            feedFetcher = new HttpURLFeedFetcher();
            feedFetcher.setUsingDeltaEncoding(true);
        }
        return feedFetcher;
    }


    public SyndFeed retrieveFeed( final String feedUrl )
        throws IOException, FeedException, FetcherException
    {
        return getFeedFetcher().retrieveFeed(new URL(feedUrl ) );
    }
}
