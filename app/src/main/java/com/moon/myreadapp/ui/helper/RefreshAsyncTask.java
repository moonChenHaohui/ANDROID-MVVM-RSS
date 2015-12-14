package com.moon.myreadapp.ui.helper;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;

import java.util.ArrayList;

/**
 * Created by moon on 15/12/14.
 */
public class RefreshAsyncTask extends SafeAsyncTask<ArrayList<Feed>, UpdateFeedEvent, String> {

    private static String TAG = RefreshAsyncTask.class.getSimpleName();


    private StatusListener listener;

    public RefreshAsyncTask(StatusListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(ArrayList<Feed>... params) {
        ArrayList<Feed> feeds = params[0];

        for (int i = 0; i < feeds.size(); i++) {

            try {
                //原来的feed feeds.get(i);
                XLog.d(TAG + "feed :" + feeds.get(i).getTitle() + " 开始更新");
                //通知正在更新这个feed
                publishProgress(new UpdateFeedEvent(feeds.get(i), UpdateFeedEvent.ON_UPDATE));

                SyndFeed syndFeed = RssHelper.getRetriever().retrieveFeed(RssHelper.adapterURL(feeds.get(i).getUrl()));
                Feed feed = DBHelper.Util.feedConert(syndFeed, DBHelper.Query.getUserId());
                ArrayList<Article> articles = DBHelper.Util.getArticles(syndFeed);
                //新更新的feed feed;
                XLog.d(TAG + "feed :" + feeds.get(i).getTitle() + " 更新完毕,共获得article:" + articles.size());
                //通知更新结束
                publishProgress(new UpdateFeedEvent(feed, UpdateFeedEvent.NORMAL));
            } catch (Exception e) {
                XLog.d(TAG + "feed :" + e);
            }

        }
        return "";
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
