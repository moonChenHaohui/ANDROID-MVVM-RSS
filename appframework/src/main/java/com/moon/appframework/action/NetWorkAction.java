package com.moon.appframework.action;

import com.moon.appframework.event.XEvent;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class NetWorkAction extends XAction{

    public String key;
    public XEvent event;

    public NetWorkAction(String key, XEvent event){
        this.key = key;
        this.event = event;
        this.type = ActionType.NETWORK;
    }
}
