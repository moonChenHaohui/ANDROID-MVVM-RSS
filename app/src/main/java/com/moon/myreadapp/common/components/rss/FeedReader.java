package com.moon.myreadapp.common.components.rss;

import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/28/15
 */
public class FeedReader implements Closeable {

    public static final int TIME_OUT = 2;//
    private FeedParser mParser = new FeedParser();

    public FeedReader() {

    }

    public Feed load(String url) throws FeedReadException {
        try {
            URL feedUrl = new URL(url);
            if (feedUrl == null) {
                throw new IllegalArgumentException("null is not a valid URL");
            } else {
                URLConnection connection = feedUrl.openConnection();
                connection.setReadTimeout(TIME_OUT*1000);//网络超时
                connection.setConnectTimeout(TIME_OUT*1000);//网络超时
                if (!(connection instanceof HttpURLConnection)) {
                    throw new IllegalArgumentException(feedUrl.toExternalForm() + " is not a valid HTTP Url");
                } else {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    InputStream inputStream = null;
                    httpConnection.connect();
                    try {
                        inputStream = httpConnection.getInputStream();
                        Feed feedSource = mParser.parse(inputStream);
                        feedSource.setUrl(url);
                        return feedSource;
                    } catch (IOException var16) {

                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }

                        httpConnection.disconnect();
                    }


                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    // TODO: 8/30/15 call me
    @Override
    public void close() throws IOException {
    }
}
