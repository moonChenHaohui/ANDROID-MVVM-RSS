package com.moon.appframework.action;

import android.os.Bundle;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class RouterAction extends XAction{

    public String url;
    public boolean isDemote = false;
    public boolean isNative = false;
    public boolean isActivity = false;
    public boolean isPattern = false;
    public Class<?> clazz;
    public Bundle args;

    /**
     *
     * 默认isDemote=false不降级,isActivity=false页面不是activity
     *
     * @param url 跳转的url
     */
    public RouterAction(String url){
        this(url, false);
    }

    /**
     *
     * @param url 跳转的url
     * @param isDemote 是否降级
     */
    public RouterAction(String url, boolean isDemote){
        this(url, isDemote, false);
    }

    /**
     *
     * @param url 跳转的url
     * @param isDemote 是否降级
     * @param isActivity 是否activity
     */
    public RouterAction(String url, boolean isDemote, boolean isActivity){
        this(url, isDemote, isActivity, false);
    }

    /**
     *
     * @param url 跳转的url
     * @param isDemote 是否降级
     * @param isActivity 是否activity
     * @param isPattern 是否正则匹配
     */
    public RouterAction(String url, boolean isDemote, boolean isActivity, boolean isPattern){
        this.url = url;
        this.isDemote = isDemote;
        this.isNative = false;
        this.isActivity = isActivity;
        this.isPattern = isPattern;
        this.type = ActionType.ROUTER;
    }

    public RouterAction(Class<?> clazz){
        this.clazz = clazz;
        this.isNative = true;
        this.isActivity = false;
        this.type = ActionType.ROUTER;
    }

    public RouterAction(Class<?> clazz, boolean isActivity){
        this.clazz = clazz;
        this.isNative = true;
        this.isActivity = isActivity;
        this.type = ActionType.ROUTER;
    }

    public RouterAction(Class<?> clazz, Bundle args){
        this.clazz = clazz;
        this.args = args;
        this.isNative = true;
        this.isActivity = false;
        this.type = ActionType.ROUTER;
    }

    public RouterAction(Class<?> clazz, Bundle args, boolean isActivity){
        this.clazz = clazz;
        this.args = args;
        this.isNative = true;
        this.isActivity = isActivity;
        this.type = ActionType.ROUTER;
    }

}
