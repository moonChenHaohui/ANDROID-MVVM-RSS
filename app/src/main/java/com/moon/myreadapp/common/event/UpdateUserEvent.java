package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.User;

/**
 * Created by moon on 16/01/06.
 */
public class UpdateUserEvent implements XEvent {

    private User user;

    public UpdateUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
