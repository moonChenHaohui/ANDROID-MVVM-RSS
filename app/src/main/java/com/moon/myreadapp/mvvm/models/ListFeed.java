package com.moon.myreadapp.mvvm.models;

import com.moon.myreadapp.common.adapter.base.FeedAdapterHelper;

/**
 * Created by moon on 15/10/23.
 * 用于封装feed,在adapter中使用
 */
public class ListFeed {

    private Feed mFeed;

    /**
     * 所属部分的位置
     */
    public int sectionPosition;
    /**
     * 在list所属位置
     */
    public int listPosition;

    /**
     * feed list 种类.初步定义这么
     */
    public FeedAdapterHelper.TimeType tpye;

    public ListFeed(Feed feed){
        this.mFeed = feed;
        //获取类型
        tpye = FeedAdapterHelper.getDayType(mFeed.getPublishTime());
    }

    public Feed getmFeed() {
        return mFeed;
    }

    public void setmFeed(Feed mFeed) {
        this.mFeed = mFeed;
    }
}
