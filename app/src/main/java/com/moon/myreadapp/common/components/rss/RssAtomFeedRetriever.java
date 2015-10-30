package com.moon.myreadapp.common.components.rss;

import com.android.volley.toolbox.Volley;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URL;

public class RssAtomFeedRetriever
{
    public SyndFeed getMostRecentNews( final String feedUrl )
    {
        try
        {
            return retrieveFeed( feedUrl );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private SyndFeed retrieveFeed( final String feedUrl )
        throws IOException, FeedException, FetcherException
    {

        FeedFetcher feedFetcher = new HttpURLFeedFetcher();
        return feedFetcher.retrieveFeed( new URL( feedUrl ) );
    }
}
