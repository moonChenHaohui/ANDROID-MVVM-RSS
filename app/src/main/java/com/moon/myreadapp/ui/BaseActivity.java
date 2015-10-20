package com.moon.myreadapp.ui;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moon.appframework.core.XActivity;
import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.LeftDrawerContentBinding;
import com.moon.myreadapp.util.ThemeUtils;

import butterknife.ButterKnife;

/**
 * Created by moon on 15/10/18.
 */
public abstract class BaseActivity extends XActivity implements IView{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //配置主题
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());

        injectView(this);
        initViewModels();
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
    }

    protected abstract
    @LayoutRes
    int getLayoutView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeViewModels();
    }
}
