package com.moon.myreadapp.util;

import android.content.Context;

import com.google.gson.Gson;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.SynchronizeStateEvent;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.mvvm.models.SyncState;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.FeedDao;
import com.moon.myreadapp.mvvm.models.dao.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by moon on 16/1/19.
 */
public class BmobHelper {

    public interface SyncListener {
        /**
         * 同步完成调用-无论成功失败
         */
        void onDatasSyncOver();

        /**
         * 单个数据同步完成
         */
        void onDataSyncSuccess();

        /**
         *
         */
        void onDataDownloadSuccess();
        void onDataSyncFailure(int i, String s);
        void onDataDownloadFailure(int i, String s);
    }

    public static String TAG = BmobHelper.class.getSimpleName();

    public static String USER_FEED = "User_Feed";
    public static String USER_ARTICLE_FAVOR = "User_Article_Favor";

    private static AtomicInteger resultCount= new AtomicInteger();
    private static AtomicInteger resultCount1= new AtomicInteger();

    /**
     * 同步频道信息
     * @param context
     */
    private static void synchronizeUserFeeds(final Context context, boolean isUpdate,final SyncListener listener) {
        final List<Feed> feeds = DBHelper.Query.getFeeds();
        int mark = 0;
        User user = DBHelper.Query.getUser();
        if (user == null) {
            return;
        }
        if (feeds == null || feeds.size() == 0) {
            return;
        }
        resultCount.set(0);
        SaveListener saveListener = new SaveListener() {

            private int count = feeds.size();
            @Override
            public void onSuccess() {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncSuccess();
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncFailure(i,s);
                XLog.d("FEED SAVE onFailure:" + s);
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
            }
        };
        UpdateListener updateListener = new UpdateListener() {
            private int count = feeds.size();
            @Override
            public void onSuccess() {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncSuccess();
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncFailure(i,s);
                XLog.d("FEED Update onFailure:" + s);
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
            }
        };

        for (int i = 0; i < feeds.size(); i++) {
            feeds.get(i).setTableName(USER_FEED);
            if (feeds.get(i).getUser() == null){
                feeds.get(i).setUser(user);
            }
            XLog.d(TAG + "FEED:" + feeds.get(i).toString());
            if (feeds.get(i).getObjectId() == null) {
                //需要插入的数据
                feeds.get(i).save(context,saveListener);
            } else if (isUpdate){
                feeds.get(i).update(context,updateListener);
            }
        }

    }

    /**
     * 更新用户频道信息
     * 1.拉取服务端数据到本地
     * 2.本地比对数据,上传未保存的数据
     *
     * @param context
     * @param start
     * @param size
     */
    public static void updateUserFeeds(final Context context, int start, int size,final SyncListener listener) {
        User user = DBHelper.Query.getUser();
        if (user == null) {
            return;
        }
        BmobQuery query = new BmobQuery(USER_FEED);

        query.addWhereEqualTo("user", DBHelper.Query.getUser());
        if (start >= 0) {
            query.setSkip(start);
        }
        if (size > 0) {
            query.setLimit(size);
        }

        query.findObjects(context, new FindCallback() {

            @Override
            public void onSuccess(JSONArray arg0) {
                if (listener != null) listener.onDataDownloadSuccess();
                XLog.d(TAG + "find onSuccess");
                Gson gson = new Gson();
                List<Feed> feeds = new ArrayList<Feed>();
                Feed feed;
                try {
                    for (int i = 0; i < arg0.length(); i++) {
                        feed = gson.fromJson(arg0.getJSONObject(i).toString(), Feed.class);
                        feed.clearBmobData();
                        feeds.add(feed);
                        XLog.d("setSyncState:回来的:" + feed.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DBHelper.UpDate.saveFeedsIfNotExist(feeds);
                //同步数据
                synchronizeUserFeeds(context,true,listener);
            }

            @Override
            public void onFailure(int i, String s) {
                if (listener != null) listener.onDataDownloadFailure(i, s);
                XLog.d(TAG + "find onFailure,msg:" + s);
            }
        });
    }





    /**
     * 同步收藏信息
     * @param context
     */
    private static void synchronizeUserFavors(final Context context, boolean isUpdate,final SyncListener listener) {
        final List<Article> favors = DBHelper.Query.getArticles(Article.Status.FAVOR,-1,-1);
        User user = DBHelper.Query.getUser();
        if (user == null) {
            return;
        }
        if (favors == null || favors.size() == 0) {
            return;
        }
        resultCount.set(0);
        SaveListener saveListener = new SaveListener() {

            private int count = favors.size();
            @Override
            public void onSuccess() {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncSuccess();
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncFailure(i,s);
                XLog.d("FEED SAVE onFailure:" + s);
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
            }
        };
        UpdateListener updateListener = new UpdateListener() {
            private int count = favors.size();
            @Override
            public void onSuccess() {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncSuccess();
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                int val = resultCount.addAndGet(1);
                if (listener != null) listener.onDataSyncFailure(i,s);
                XLog.d("FEED Update onFailure:" + s);
                if (listener != null && val>= count -1){
                    listener.onDatasSyncOver();
                }
            }
        };
        for (int i = 0; i < favors.size(); i++) {
            favors.get(i).setTableName(USER_ARTICLE_FAVOR);
            if (favors.get(i).getUser() == null){
                favors.get(i).setUser(user);
            }
            XLog.d(TAG + "favors:" + favors.get(i).toString());
            if (favors.get(i).getObjectId() == null) {
                //需要插入的数据
                //
                favors.get(i).save(context,saveListener);
                //feeds.remove(i);
            } else if (isUpdate){
                favors.get(i).update(context,updateListener);
            }
        }

    }

    /**
     * 更新用户收藏信息
     * 1.拉取服务端数据到本地
     * 2.本地比对数据,上传未保存的数据
     *
     * @param context
     * @param start
     * @param size
     */
    public static void updateUserFavors(final Context context, int start, int size,final SyncListener listener) {
        User user = DBHelper.Query.getUser();
        if (user == null) {
            return;
        }
        BmobQuery query = new BmobQuery(USER_ARTICLE_FAVOR);

        query.addWhereEqualTo("user", DBHelper.Query.getUser());
        if (start >= 0) {
            query.setSkip(start);
        }
        if (size > 0) {
            query.setLimit(size);
        }

        query.findObjects(context, new FindCallback() {

            @Override
            public void onSuccess(JSONArray arg0) {
                if (listener != null) listener.onDataDownloadSuccess();
                XLog.d(TAG + "find onSuccess");
                Gson gson = new Gson();
                List<Article> articles = new ArrayList<Article>();
                Article article;
                try {
                    for (int i = 0; i < arg0.length(); i++) {
                        article = gson.fromJson(arg0.getJSONObject(i).toString(), Article.class);
                        article.clearBmobData();
                        articles.add(article);
                        //XLog.d("setSyncState:回来的:" + article.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DBHelper.UpDate.saveArticlesIfNotExist(articles);
                //同步数据
                synchronizeUserFavors(context, true,listener);
            }

            @Override
            public void onFailure(int i, String s) {
                if (listener != null) listener.onDataDownloadFailure(i,s);
                XLog.d(TAG + "find onFailure,msg:" + s);
            }
        });
    }


}
