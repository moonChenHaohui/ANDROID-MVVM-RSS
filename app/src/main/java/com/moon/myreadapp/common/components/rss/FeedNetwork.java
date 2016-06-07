package com.moon.myreadapp.common.components.rss;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;

import java.util.List;


/**
 * @description:
 * @author: Match
 * @date: 8/28/15
 */
public class FeedNetwork {
    private static final int MSG_NOTIFY_FEED_DB_UPDATED = 0;
    private static final int DELAY_NOTIFY_FEED_DB_UPDATED = 1000;
    private static FeedNetwork sInstance;
    private FeedReader mFeedReader;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_NOTIFY_FEED_DB_UPDATED:
                    //todo 通知ui更新
                    //EventBus.getDefault().post(CommonEvent.FEED_DB_UPDATED);
                    break;
                default:
            }
        }
    };

    private FeedNetwork() {
    }

    public static FeedNetwork getInstance() {
        if (sInstance == null) {
            sInstance = new FeedNetwork();
            sInstance.mFeedReader = new FeedReader();
        }
        return sInstance;
    }

    public void refreshAll() {
        List<Feed> feedSourceList = DBHelper.Query.getFeeds();
        for (Feed source : feedSourceList) {
            refresh(source.getId());
        }
    }

    public void refresh(final long sourceId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Feed oldFeedSource = DBHelper.Query.getFeed(sourceId);
                    Feed newFeedSource = mFeedReader.load(oldFeedSource.getUrl());
                    DBHelper.UpDate.saveFeed(newFeedSource);
                    notifyUI();
                } catch (FeedReadException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public interface OnVerifyListener {
        void onResult(boolean isValid, Feed feedSource);
    }

    public void verifySource(final String url, final OnVerifyListener listener) {
        new AsyncTask<Void, Void, Feed>() {
            @Override
            protected Feed doInBackground(Void... params) {
                try {
                    Feed newFeedSource = mFeedReader.load(url);
                    if (TextUtils.isEmpty(newFeedSource.getTitle())) {
                        return null;
                    }
                    // TODO: 8/29/15 need more verify ?
                    return newFeedSource;
                } catch (FeedReadException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Feed feedSource) {
                super.onPostExecute(feedSource);
                if (listener != null) {
                    listener.onResult(feedSource != null, feedSource);
                }
            }
        }.execute();
    }

    public interface OnAddListener {
        void onError(String msg);
    }

    public void addSource(final String url, final String reTitle, OnAddListener listener) {
        if (null != DBHelper.Query.findFeedByURL(url)) {
            if (listener != null) {
                listener.onError("源已经添加过了");
            }
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Feed feedSource = mFeedReader.load(url);
                    feedSource.setTitle(reTitle);
                    DBHelper.Insert.feed(feedSource);
                    //todo 得到返回的文章
                    DBHelper.Insert.articles(null);
                   // FeedDB.getInstance().saveFeedSource(feedSource);
                   // FeedDB.getInstance().saveFeedItem(feedSource.getFeedItems(), feedSource.getId());
                    notifyUI();
                } catch (FeedReadException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void addSource(final Feed feedSource, OnAddListener listener) {
        if (DBHelper.Query.hasExistFeed(feedSource)) {
            if (listener != null) {
                listener.onError("源已经添加过了");
            }
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DBHelper.Insert.feed(feedSource);
                //todo 得到返回的文章
                DBHelper.Insert.articles(null);
                notifyUI();
                return null;
            }
        }.execute();
    }

    private void notifyUI() {
        mHandler.removeMessages(MSG_NOTIFY_FEED_DB_UPDATED);
        mHandler.sendEmptyMessageDelayed(MSG_NOTIFY_FEED_DB_UPDATED, DELAY_NOTIFY_FEED_DB_UPDATED);
    }
}
