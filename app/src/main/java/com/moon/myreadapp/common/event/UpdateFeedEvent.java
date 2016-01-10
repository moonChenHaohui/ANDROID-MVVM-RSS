package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;

/**
 * Created by moon on 15/11/13.
 */
public class UpdateFeedEvent implements XEvent {

    public enum TYPE {
        STATUS,
        SET
    }

    private Feed feed;
    private int status = NORMAL;
    private boolean showAllArticles;
    private String notice;
    private int updatePosition = -1;
    private Article article;

    /** 更新状态*/
    public static int ON_UPDATE = 1<<10;
    /** 正常状态*/
    public static int NORMAL    = 1<<11;


    private TYPE type;

    public UpdateFeedEvent(Feed feed,TYPE type) {
        this.feed = feed;
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isShowAllArticles() {
        return showAllArticles;
    }

    public void setShowAllArticles(boolean showAllArticles) {
        this.showAllArticles = showAllArticles;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Feed getFeed() {
        return feed;
    }

    public int getStatus() {
        return status;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getUpdatePosition() {
        return updatePosition;
    }

    public void setUpdatePosition(int updatePosition) {
        this.updatePosition = updatePosition;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
