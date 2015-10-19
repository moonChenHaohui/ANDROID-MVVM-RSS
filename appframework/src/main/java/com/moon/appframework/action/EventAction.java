package com.moon.appframework.action;


import com.moon.appframework.event.XEvent;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class EventAction extends XAction{

    public XEvent event;

    public EventAction(XEvent event){
        this.event = event;
        this.type = ActionType.EVENT;
    }
}
