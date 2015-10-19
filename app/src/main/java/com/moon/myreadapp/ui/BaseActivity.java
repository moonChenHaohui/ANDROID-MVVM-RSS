package com.moon.myreadapp.ui;


import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.moon.appframework.core.XActivity;
import com.moon.myreadapp.util.ThemeUtils;

import butterknife.ButterKnife;

/**
 * Created by moon on 15/10/18.
 */
public abstract class BaseActivity extends XActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //配置主题
        //initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
    }

    private void initTheme(){
        ThemeUtils.Theme theme = ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changTheme(this, theme);
    }
    protected abstract @LayoutRes
    int getLayoutView();
}
