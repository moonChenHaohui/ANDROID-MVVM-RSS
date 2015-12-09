package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;

/**
 * Created by moon on 15/11/13.
 */
public class UpdateArticleEvent implements XEvent {

    private int useCount;
    private int position;

    public UpdateArticleEvent(int useCount, int position) {
        this.useCount = useCount;
        this.position = position;
    }

    public int getUseCount() {
        return useCount;
    }

    public int getPosition() {
        return position;
    }
}
