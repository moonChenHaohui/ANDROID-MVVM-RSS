package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;

/**
 * Created by moon on 15/11/13.
 */
public class UpdateFeedEvent implements XEvent {

    private Feed feed;
    private int status = NORMAL;

    /** 更新状态*/
    public static int ON_UPDATE = 1<<10;
    /** 正常状态*/
    public static int NORMAL    = 1<<11;

    public UpdateFeedEvent(Feed feed) {
        this.feed = feed;
    }
    public UpdateFeedEvent(Feed feed, int status) {
        this.feed = feed;
        this.status =status;
    }

    public Feed getFeed() {
        return feed;
    }

    public int getStatus() {
        return status;
    }
}
