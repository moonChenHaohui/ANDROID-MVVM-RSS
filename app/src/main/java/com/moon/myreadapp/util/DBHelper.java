package com.moon.myreadapp.util;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.module.DCModule;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.application.ReadApplication;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.ArticleDao;
import com.moon.myreadapp.mvvm.models.dao.DaoSession;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.FeedDao;
import com.moon.myreadapp.mvvm.models.dao.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by moon on 15/11/9.
 */
public class DBHelper {

    public static synchronized DaoSession getDAO (){
        return ((ReadApplication)ReadApplication.getInstance()).getDaoSession();
    }


    public static class Util {
        public static Feed feedConert(SyndFeed syndFeed,long userID){
            Feed feed = new Feed(null,
                    syndFeed.getTitle(),
                    null,//url
                    0,
                    0,
                    syndFeed.getDescription(),
                    syndFeed.getFeedType(),
                    syndFeed.getLink(),
                    HtmlHelper.getIconUrlString(syndFeed.getLink()),//icon获取
                    syndFeed.getPublishedDate(),
                    null,
                    null,//最近的图片
                    syndFeed.getLanguage(),
                    ((DCModule) syndFeed.getModules().get(0)).getRights(),
                    ((DCModule) syndFeed.getModules().get(0)).getUri(),
                    ((DCModule) syndFeed.getModules().get(0)).getCreator(),
                    userID);
            return feed;
        }

        public static ArrayList<Article> getArticles(SyndFeed syndFeed){
            List<SyndEntry> list = syndFeed.getEntries();
            ArrayList<Article> article = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                article.add(articleConvert(list.get(i)));
            }
            return article;
        }

        public static Article articleConvert(SyndEntry entry){
            Article article = new Article(
                    null,
                    entry.getTitle(),
                    0,
                    entry.getDescription() == null ? "" :entry.getDescription().getValue(),
                    entry.getLink(),
                    entry.getDescription() == null ? null : StringHelper.convertListToSrring(HtmlHelper.getImgStr(entry.getDescription().getValue(),3)),//获取最开始的3张图片
                    entry.getPublishedDate(),
                    null,//最近阅读时间
                    0,//状态
                    ((DCModule) entry.getModules().get(0)).getUri(),
                    ((DCModule) entry.getModules().get(0)).getRights(),
                    ((DCModule) entry.getModules().get(0)).getCreator(),
                    -1);
            return article;
        }
    }

    public static class Insert{

        public static long feed(Feed feed){
            long id = getDAO().getFeedDao().insert(feed);
            XLog.d("insert feed id :" + id);
            return id;
        }
        public static long article(Article article){
            if (article.getPublishtime() == null){
                article.setPublishtime(new Date());
            }
            long id = getDAO().getArticleDao().insertOrReplace(article);
            return id;
        }
        public static void articles(List<Article> articles){
            if (articles == null) return;
            for (int i = 0; i< articles.size();i++){
                article(articles.get(i));
            }
        }
    }

    public static class Query {
        /**
         * 用户是否订阅过这个feed
         * @param feed
         * @return
         */
        public static boolean hasExistFeed (Feed feed){
            return getDAO().getFeedDao().queryBuilder().where(FeedDao.Properties.Uri.eq(feed.getUri())).list().size() > 0;
        }

        public static User getUser(){
            if (getDAO().getUserDao().queryBuilder().list().size() > 0){
                return getDAO().getUserDao().queryBuilder().list().get(0);
            }
            return null;
        }
        public static List<Feed> getFeeds(){
            return getDAO().getFeedDao().queryBuilder().list();
        }

        public static List<Article> getArticles(){
            return getDAO().getArticleDao().queryBuilder().list();
        }

        public static List<Article> getArticlesByID(long id,Article.Status status){
            QueryBuilder<Article> res =  getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Feed_id.eq(id));
            if (status != null){
                WhereCondition wc;
                if (status == Article.Status.NORMAL_AND_FAVOR){
                    wc = ArticleDao.Properties.Status.in(Article.Status.NORMAL.status,Article.Status.FAVOR.status);
                } else {
                    wc = ArticleDao.Properties.Status.eq(status.status);
                }
                return res.where(wc).orderDesc(ArticleDao.Properties.Publishtime).list();
            }
            return res.orderDesc(ArticleDao.Properties.Publishtime).list();
        }

        public static Article getArticle (long id){
            List<Article> list = getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Id.eq(id)).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list.get(0);
        }
        public static Article getRecentArticleOnFeedByFeedId (long id){
            List<Article> list = getDAO().getArticleDao().queryBuilder().
                    where(ArticleDao.Properties.Feed_id.eq(id)).orderDesc(ArticleDao.Properties.Publishtime).limit(1).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list.get(0);
        }

        public static Feed getFeed (long id){
            List<Feed> list = getDAO().getFeedDao().queryBuilder().where(FeedDao.Properties.Id.eq(id)).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list.get(0);
        }

        public static long getUserId(){
            return getUser() != null ? getUser().getId() : -1;
        }

        public static long getFeedUnReadByFeedId (long id){
            return getDAO().getArticleDao().queryBuilder().
                    where(
                            ArticleDao.Properties.Feed_id.eq(id),
                            ArticleDao.Properties.Use_count.eq(0),
                            ArticleDao.Properties.Status.notEq(Article.Status.DELETE
                            )).count();
        }

    }

    public static class UpDate{

        public static long saveArticle(Article article){
            return getDAO().getArticleDao().insertOrReplace(article);
        }
        public static long saveFeed(Feed feed){
            return getDAO().getFeedDao().insertOrReplace(feed);
        }
        public static long saveUser(User user){
            //只保存一个用户信息
            getDAO().getUserDao().deleteAll();
            return getDAO().getUserDao().insertOrReplace(user);
        }
    }

}
