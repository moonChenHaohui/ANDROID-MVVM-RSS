package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;

/**
 * Created by moon on 15/11/13.
 */
public class UpdateArticleEvent implements XEvent {

    private Feed feed;

    public UpdateArticleEvent(Feed feed) {
        this.feed = feed;
    }

    public Feed getFeed() {
        return feed;
    }
}
