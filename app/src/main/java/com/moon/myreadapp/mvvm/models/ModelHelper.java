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
        ArrayList<Article> result = new ArrayList<Article>();
        //如果没有找出,则把所有添加进去.
        if (recentArticle == null){
            for (int i = 0; i < articles.size(); i++) {
                articles.get(i).setFeed_id(feedId);
                result.add(articles.get(i));
            }
        } else {
            Date date = recentArticle.getPublishtime();
            Date temp;
            for (int i = 0; i < articles.size(); i++) {
                articles.get(i).setFeed_id(feedId);
                temp = articles.get(i).getPublishtime();
                //如果数据中没有日期,则直接加入;如果有,则加入日期之后的
                if (date == null || (temp != null && temp.after(date))) {
                    result.add(articles.get(i));
                }
            }
        }

        return result;
    }
}
