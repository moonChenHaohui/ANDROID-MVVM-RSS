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
            long id = getDAO().getFeedDao().insertOrReplace(feed);
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



        public static List<Article> getArticlesByID(long id,Article.Status status,int start,int size){
            QueryBuilder<Article> res =  getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Feed_id.eq(id));
            if (status != null){
                WhereCondition wc;
                if (status == Article.Status.NORMAL_AND_FAVOR || status == Article.Status.NORMAL_AND_FAVOR_BUT_UNREAD) {
                    wc = ArticleDao.Properties.Status.in(Article.Status.NORMAL.status, Article.Status.FAVOR.status);

                } else {
                    wc = ArticleDao.Properties.Status.eq(status.status);
                }
                if (status == Article.Status.NORMAL_AND_FAVOR_BUT_UNREAD){
                    res = res.where(ArticleDao.Properties.Use_count.le(0));
                }
                return res.where(wc).orderDesc(ArticleDao.Properties.Publishtime).offset(start).limit(size).list();
            }
            return res.orderDesc(ArticleDao.Properties.Publishtime).offset(start).limit(size).list();
        }

        /**
         * 根据状态查询文章
         * @param status
         * @param start
         * @param size
         * @return
         */
        public static List<Article> getArticles(Article.Status status,int start,int size){
            QueryBuilder<Article> res =  getDAO().getArticleDao().queryBuilder();
            if (status != null){
                WhereCondition wc  = ArticleDao.Properties.Status.eq(status.status);
                return res.where(wc).orderDesc(ArticleDao.Properties.Publishtime).offset(start).limit(size).list();
            }
            return res.orderDesc(ArticleDao.Properties.Publishtime).offset(start).limit(size).list();
        }

        /**
         * 获取文章阅读历史
         * @param start
         * @param size
         * @return
         */
        public static List<Article> getArticlesReadHistory(int start,int size){
            QueryBuilder<Article> res =  getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Status.notEq(Article.Status.DELETE.status),
                    ArticleDao.Properties.Use_count.gt(0));
            return res.orderDesc(ArticleDao.Properties.Last_read_time).offset(start).limit(size).list();
        }

        public static List<Article> getArticlesUnRead(int start,int size){
            QueryBuilder<Article> res =  getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Status.notEq(Article.Status.DELETE.status),
                    ArticleDao.Properties.Use_count.eq(0));
            return res.orderDesc(ArticleDao.Properties.Publishtime).offset(start).limit(size).list();
        }
        public static List<Article> getArticlesReadedAndUnFavor(){
            List<Article> list =  getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Status.in(Article.Status.NORMAL.status, Article.Status.DELETE.status),
                    ArticleDao.Properties.Use_count.gt(0)).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list;
        }
        public static int getArticlesCountReadedAndUnFavor(){
            List<Article> list =  getArticlesReadedAndUnFavor();
            if (list == null){
                return 0;
            }
            return list.size();
        }



        public static Article getArticle (long id){
            List<Article> list = getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Id.eq(id)).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list.get(0);
        }
        public static List<Article> getArticlesByFeedId (long id){
            List<Article> list = getDAO().getArticleDao().queryBuilder().
                    where(ArticleDao.Properties.Feed_id.eq(id)).list();
            if (null == list || list.size() == 0){
                return null;
            }
            return list;
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

        public static Feed findFeedByURL(String url){
            if (url == null){
                return null;
            }
            List<Feed> list = getDAO().getFeedDao().queryBuilder().where(FeedDao.Properties.Url.eq(url)).list();
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
                            ArticleDao.Properties.Use_count.le(0),
                            ArticleDao.Properties.Status.notEq(Article.Status.DELETE.status
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
        public static void readAllArticleFromFeed(Feed feed){
            List<Article> articles = getDAO().getArticleDao().queryBuilder().where(ArticleDao.Properties.Feed_id.eq(feed.getId())).list();
            Date date = new Date();
            for (Article a:articles){
                a.setUse_count(1);
                a.setLast_read_time(date);
                saveArticle(a);
            }
        }
        public static long saveUser(User user){
            //只保存一个用户信息
            getDAO().getUserDao().deleteAll();
            return getDAO().getUserDao().insertOrReplace(user);
        }
    }

    public static class Delete {
        public static boolean deleteFeed(Feed feed){
            Feed f =Query.findFeedByURL(feed.getUrl());
            if (f != null) {
                //先删除feed内文章
                deleteArticlesByFeed(f);

                getDAO().getFeedDao().delete(f);
            }
            return true;
        }
        private static boolean deleteArticlesByFeed(Feed feed){
            List<Article> articles = Query.getArticlesByFeedId(feed.getId());
            getDAO().getArticleDao().deleteInTx(articles);
            return true;
        }

        public static boolean deleteArticleReadedAndUnFavor(){
            List<Article> articles = Query.getArticlesReadedAndUnFavor();
            getDAO().getArticleDao().deleteInTx(articles);
            return true;
        }
    }

}
