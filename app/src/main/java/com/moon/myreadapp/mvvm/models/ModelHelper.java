package com.moon.myreadapp.mvvm.models;

import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.util.DBHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by moon on 15/12/15.
 */
public class ModelHelper {


    /**
     * 从获取的文章中找出新更新的
     * @param feedId
     * @param articles
     * @return
     */
    public static ArrayList<Article> getUpDateArticlesByFeedId (long feedId,ArrayList<Article> articles){
        if(articles == null ||articles.size() ==0){
            return null;
        }
        Article recentArticle = DBHelper.Query.getRecentArticleOnFeedByFeedId(feedId);
        Date date = recentArticle.getPublishtime();
        Date temp;
        ArrayList<Article> result = new ArrayList<Article>();
        for (int i = 0; i < articles.size(); i++) {
            articles.get(i).setFeed_id(feedId);
            temp = articles.get(i).getPublishtime();
            if (temp != null && temp.after(date)) {
                result.add(articles.get(i));
            }
        }
        return result;
    }
}
