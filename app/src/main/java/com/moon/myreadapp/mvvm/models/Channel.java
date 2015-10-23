package com.moon.myreadapp.mvvm.models;

import java.io.Serializable;

/**
 * Created by moon on 15/10/20.
 * 频道 model
 */
public class Channel implements Serializable {

    /**
     * 频道介绍
     */
    protected String info;

    private int type;

    public int sectionPosition;
    public int listPosition;

    public Channel(String info,int type) {
        // super(builder);
        this.info = info;
        this.type =type;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
