package com.moon.myreadapp.mvvm.models;

/**
 * Created by moon on 15/10/20.
 * 频道 model
 */
public class Channel {

    /**
     * 频道介绍
     */
    protected String info;


    public Channel(String info) {
        // super(builder);
        this.info = info;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
