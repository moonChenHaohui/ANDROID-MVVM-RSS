package com.moon.myreadapp.read.homepage.viewhelper;

import android.support.v4.app.Fragment;

import com.moon.myreadapp.read.homepage.fragment.HomeFragment;
import com.moon.myreadapp.read.me.fragment.MeFragment;

/**
 * Created by moon on 15/10/1.
 */
public enum  TabState {

    HOMEPAGE("主页", HomeFragment.class),ME("我", MeFragment.class);

    private String name;
    private Class clazz;
    private TabState (String name,Class clazz){
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public static TabState from(TabState tabState) {
        return fromInt(tabState.ordinal());
    }
    public static TabState fromInt(int value) {
        if (HOMEPAGE.ordinal() == value) {
            return HOMEPAGE;
        }
        if (ME.ordinal() == value) {
            return ME;
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
