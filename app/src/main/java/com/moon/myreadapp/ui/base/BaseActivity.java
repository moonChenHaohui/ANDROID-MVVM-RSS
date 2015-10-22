package com.moon.myreadapp.ui.base;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;

import com.moon.appframework.core.XActivity;
import com.moon.myreadapp.R;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.ThemeUtils;

/**
 * Created by moon on 15/10/18.
 */
public abstract class BaseActivity extends XActivity implements IView {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //配置主题
        initTheme();
        super.onCreate(savedInstanceState);
        setContentViewAndBindVm(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        injectView(this);

    }

    private void initTheme() {
        ThemeUtils.Theme theme = ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changTheme(this, theme);
    }

    /**
     * 该方法提供统一设置toolbar的方式;  可以继承 ToolBarExpandActivity,可以直接再顶部视图中设置一个toolbar
     * @param toolbar
     */
    protected void initToolBar (Toolbar toolbar){
        if(toolbar == null) return;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.action_bar_title_color));
        toolbar.collapseActionView();
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract @LayoutRes int getLayoutView();
}
