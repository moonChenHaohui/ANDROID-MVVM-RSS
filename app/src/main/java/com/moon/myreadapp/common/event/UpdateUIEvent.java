package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Feed;

/**
 * Created by moon on 15/12/16.
 */
public class UpdateUIEvent implements XEvent {


    /** 主题更改*/
    public static int THEME_CHANGE = 1<<10;

    private int status;
    public UpdateUIEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
