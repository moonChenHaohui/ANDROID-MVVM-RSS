package com.moon.myreadapp.ui;

import com.moon.appframework.event.XEvent;

/**
 * Created by moon on 15/10/23.
 */
public class AEvent implements XEvent {

    private String a;
    public AEvent (String a){
        this.a = a;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
