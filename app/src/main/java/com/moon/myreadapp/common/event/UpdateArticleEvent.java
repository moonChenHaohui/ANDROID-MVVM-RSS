package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Feed;

/**
 * Created by moon on 16/1/4.
 */
public class UpdateArticleEvent implements XEvent {

    private int fontSize;


    public UpdateArticleEvent(int size){
        fontSize = size;
    }

    public int getFontSize() {
        return fontSize;
    }
}
