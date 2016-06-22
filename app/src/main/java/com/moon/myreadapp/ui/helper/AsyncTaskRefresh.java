package com.moon.myreadapp.ui.helper;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.common.components.rss.FeedNetwork;
import com.moon.myreadapp.common.components.rss.FeedReader;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.mvvm.models.ModelHelper;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by moon on 16/06/13.
 */
public class AsyncTaskRefresh extends SafeAsyncTask<ArrayList<Feed>, UpdateFeedEvent, String> {

    private static String TAG = AsyncTaskRefresh.class.getSimpleName();


    private StatusListener listener;

    public AsyncTaskRefresh(StatusListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(ArrayList<Feed>... params) {
        ArrayList<Feed> feeds = params[0];
        if(feeds == null || feeds.size() == 0){
            return "";
        }
        Timer timer;
        for (int i = 0; i < feeds.size() && !isCancelled(); i++) {
            if (isCancelled()){
                return "";
            }
            try {
                //原来的feed feeds.get(i);
                XLog.d(TAG + "feed :" + feeds.get(i).getTitle() + " 开始更新");
                //通知正在更新这个feed
                sentEvent(feeds.get(i),UpdateFeedEvent.ON_UPDATE,"开始更新....");

                Feed feed = FeedNetwork.getInstance().load(feeds.get(i).getUrl());

                //转换出文章list
                List<Article> articles = feed.getArticles();
                //过滤,获取新数据;
                ArrayList<Article> result = ModelHelper.getUpDateArticlesByFeedId(feeds.get(i).getId(),articles);
                XLog.d(TAG + "feed :" + feeds.get(i).getTitle() + "id : " + feeds.get(i).getId()+ " 更新完毕,共获得更新的文章:" + result.size());
                //插入数据
                DBHelper.Insert.articles(result,feeds.get(i).getId());
                //DBHelper.Insert.feed(feed);
                //通知更新结束
                sentEvent(feeds.get(i),UpdateFeedEvent.NORMAL,"更新完毕....");
            } catch (Exception e) {
                XLog.d(TAG + "feed :" + e);
                //更新失败...
                sentEvent(feeds.get(i),UpdateFeedEvent.FAIL,"更新失败....");
            }

        }
        return "";
    }


    private void sentEvent(Feed feed,final int status,final String notice){
        UpdateFeedEvent event1 =  new UpdateFeedEvent(feed, UpdateFeedEvent.TYPE.STATUS);
        event1.setStatus(status);
        event1.setNotice(notice);
        publishProgress(event1);
    }
    @Override
    protected void onSafePostExecute(String s) {
        super.onSafePostExecute(s);
        listener.onSuccess();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        listener.onCancel();
    }

    @Override
    protected void onProgressUpdate(UpdateFeedEvent... values) {
        UpdateFeedEvent progress = values[0];
        XApplication.getInstance().bus.post(progress);
    }

    public interface StatusListener {
        void onSuccess();

        void onCancel();
    }


}
