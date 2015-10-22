package com.moon.myreadapp.ui.base.IViews;

/**
 * Created by moon on 15/10/19.
 */
public interface IMainView {

    /**
     * 下拉加载完成
     */
    void onPullDownRefreshComplete();

    /**
     * 上拉加载完成
     */
    void onPullUpRefreshComplete();

}
