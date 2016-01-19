package com.moon.myreadapp.util;

import android.content.Context;

import com.google.gson.Gson;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by moon on 16/1/19.
 */
public class BmobHelper {

    public static String USER_FEED = "User_Feed";
    public static String USER_ARTICLE_FAVOR = "User_Article_Favor";

    /**
     * 同步频道信息
     * 1.将本地频道信息post到服务端
     * 2.拉取服务端信息
     * @param context
     */
    public static void synchronizeUserFeeds(final Context context){
        final List<Feed> feeds = DBHelper.Query.getFeeds();
        User user = DBHelper.Query.getUser();
        if (user == null){
            return;
        }
        if (feeds == null || feeds.size() == 0){
            updateUserFeeds(context, -1, -1);
            return;
        }

        SaveListener listener = new SaveListener() {

            private int mark = 0;

            private int feedCount = feeds.size();
            @Override
            public void onSuccess() {
                mark++;
                //下下策,当更新到最后一个数据的时候,通知本地更新正确的服务端数据
                if (mark >= feedCount){
                    updateUserFeeds(context, -1, -1);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                mark++;
                //下下策,当更新到最后一个数据的时候,通知本地更新正确的服务端数据
                if (mark >= feedCount){
                    updateUserFeeds(context, -1, -1);
                }
            }
        };
        XLog.d("BmobHelper : 提交本地feed 数据,共提交feed:" + feeds.size());
        int size = feeds.size();
        Feed tempFeed;
        for (int i = 0;i < size;i++){
            tempFeed = feeds.get(i);
            if (tempFeed.getUser() == null){
                tempFeed.setUser(user);
            }
            //一定要设置tablename
            tempFeed.setTableName(USER_FEED);
            tempFeed.save(context, listener);
        }
    }

    /**
     * 更新用户频道信息
     * @param context
     * @param start
     * @param size
     */
    public static void updateUserFeeds(final Context context,int start,int size){
        User user = DBHelper.Query.getUser();
        if (user == null){
            return;
        }
        BmobQuery query = new BmobQuery(USER_FEED);

        query.addWhereEqualTo("user", DBHelper.Query.getUser());
        if (start >=0){
            query.setSkip(start);
        }
        if (size >0){
            query.setLimit(size);
        }

        query.findObjects(context,  new FindCallback() {

            @Override
            public void onSuccess(JSONArray arg0) {
                Gson gson = new Gson();
                List<Feed> feeds = new ArrayList<Feed>();

                try {
                    for (int i = 0;i < arg0.length();i++){

                        feeds.add(gson.fromJson(arg0.getJSONObject(i).toString(),Feed.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DBHelper.UpDate.saveFeeds(feeds);
                XApplication.getInstance().bus.post(new UpdateFeedListEvent());
                ToastHelper.showToast("更新本地数据成功!");

                XLog.d("BmobHelper : 拉取用户feed信息,共拉取:" + feeds.size());
                synchronizeUserFavors(context);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 同步收藏
     * @param context
     */
    public static void synchronizeUserFavors(final Context context){
        final List<Article> articles = DBHelper.Query.getArticles(Article.Status.FAVOR, -1, -1);
        User user = DBHelper.Query.getUser();
        if (user == null){
            return;
        }
        if (articles == null || articles.size() == 0){
            updateUserFavors(context, -1, -1);
            return;
        }


        SaveListener listener = new SaveListener() {

            private int mark = 0;

            private int articleCount = articles.size();
            @Override
            public void onSuccess() {
                mark++;
                //下下策,当更新到最后一个数据的时候,通知本地更新正确的服务端数据
                if (mark >= articleCount){
                    updateUserFavors(context, -1, -1);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                mark++;
                //下下策,当更新到最后一个数据的时候,通知本地更新正确的服务端数据
                if (mark >= articleCount){
                    updateUserFavors(context, -1, -1);
                }
            }
        };
        int size = articles.size();

        XLog.d("BmobHelper : 提交本地收藏的文章 数据,共提交:" + size);
        for (int i = 0;i < size;i++){
            if (articles.get(i).getUser() == null){
                articles.get(i).setUser(user);
            }
            //一定要设置tablename
            articles.get(i).setTableName(USER_ARTICLE_FAVOR);
            articles.get(i).save(context, listener);
        }
    }

    /**
     * 更新用户收藏
     * @param context
     * @param start
     * @param size
     */
    public static void updateUserFavors(Context context,int start,int size){
        User user = DBHelper.Query.getUser();
        if (user == null){
            return;
        }
        //这里设置获取的表名USER_ARTICLE_FAVOR
        BmobQuery query = new BmobQuery(USER_ARTICLE_FAVOR);
        query.addWhereEqualTo("user", DBHelper.Query.getUser());
        if (start >=0){
            query.setSkip(start);
        }
        if (size >0){
            query.setLimit(size);
        }
        query.findObjects(context, new FindCallback() {

            @Override
            public void onSuccess(JSONArray arg0) {
                Gson gson = new Gson();
                List<Article> articles = new ArrayList<Article>();

                try {
                    for (int i = 0;i < arg0.length();i++){

                        articles.add(gson.fromJson(arg0.getJSONObject(i).toString(),Article.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DBHelper.UpDate.saveArticles(articles);
                XLog.d("BmobHelper : 拉取用户收藏的文章信息,共拉取:" + articles.size());
                ToastHelper.showToast("更新本地数据成功!");
            }

            @Override
            public void onFailure(int arg0, String arg1) {

            }
        });
    }

}
