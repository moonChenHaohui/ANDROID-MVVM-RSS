package com.moon.myreadapp.ui;

import com.moon.myreadapp.mvvm.models.BaseMenuItem;

import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public interface IMainView {

    /**
     * 初始化drawer
     * @param menus
     */
    void initDrawer(List<BaseMenuItem> menus);
}
