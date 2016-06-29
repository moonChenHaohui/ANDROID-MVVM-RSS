package com.moon.myreadapp.common.components.rss;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.moon.appframework.action.XAction;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.mvvm.models.ModelHelper;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.FeedDao;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.Globals;

import java.util.ArrayList;
import java.util.List;


/**
 * @description:
 * @author: Match
 * @date: 8/28/15
 *
 * 修改  moon
 */
public class FeedNetwork {
    private static FeedNetwork sInstance;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    private FeedReader mFeedReader;


    private FeedNetwork() {
    }

    public static FeedNetwork getInstance() {
        if (sInstance == null) {
            sInstance = new FeedNetwork();
            sInstance.mFeedReader = new FeedReader();
        }
        return sInstance;
    }

    public void refreshAll(OnRefreshListener listener) {
        List<Feed> feedSourceList = DBHelper.Query.getFeeds();
        for (Feed source : feedSourceList) {
            refresh(source.getId(),listener);
        }
    }

    public void refresh(final long sourceId,final OnRefreshListener listener) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Feed oldFeedSource = DBHelper.Query.getFeed(sourceId);
                    final Feed newFeedSource = mFeedReader.load(oldFeedSource.getUrl());
                    if (null == newFeedSource){
                        if (listener != null) {
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError(BuiltConfig.getString(R.string.dialog_sub_search_error));
                                }
                            });
                        }
                        return null;
                    }
                    newFeedSource.setId(sourceId);
                    DBHelper.UpDate.saveFeed(newFeedSource);
                    //result 为获取新更新的文章
                    final ArrayList<Article> result = ModelHelper.getUpDateArticlesByFeedId(sourceId, newFeedSource.getArticles());
                    //插入数据库
                    if (result != null && result.size() > 0) {
                        DBHelper.Insert.articles(result,sourceId);
                    }
                    if (listener != null) {
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSuccess(newFeedSource,result);
                            }
                        });
                    }
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

    public interface OnRefreshListener {
        void onError(String msg);
        void onSuccess(Feed feed,ArrayList<Article> list);
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
        void onSuccess();
    }

    public void addSource(String url, final String reTitle, final OnAddListener listener) {
        synchronized (listener) {
            if (null != DBHelper.Query.findFeedByURL(url)) {
                if (listener != null) {
                    listener.onError(BuiltConfig.getString(R.string.sub_notice_already_exist));
                }
                return;
            }
            if (!RssHelper.isURL(url)) {
                if (listener != null) {
                    listener.onError(BuiltConfig.getString(R.string.dialog_sub_search_error));
                }
                return;
            }
            final String curl = RssHelper.adapterURL(url);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Feed feedSource = mFeedReader.load(curl);
                        if (null == feedSource) {
                            if (listener != null) {
                                listener.onError(BuiltConfig.getString(R.string.dialog_sub_search_error));
                            }
                            return null;
                        }
                        if (null != reTitle) {
                            feedSource.setTitle(reTitle);
                        }
                        long id = DBHelper.Insert.feed(feedSource);
                        List<Article> articles = feedSource.getArticles();
                        if (null != articles && articles.size() > 0) {
                            DBHelper.Insert.articles(articles, id);
                        }
                        if (null != listener) {
                            listener.onSuccess();
                        }
                        notifyUI();
                    } catch (FeedReadException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }

    public Feed load (String url){
        try {
            return mFeedReader.load(url);
        } catch (FeedReadException e) {
            return null;
        }
    }
    private void notifyUI() {
        XApplication.getInstance().bus.post(new UpdateFeedListEvent());
    }
}
