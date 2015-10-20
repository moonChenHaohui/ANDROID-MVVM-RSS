package com.moon.myreadapp.ui;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;

import com.moon.myreadapp.ui.helper.ToolBarHelper;

/**
 * Created by moon on 15/10/19.
 * 封装了toolbar.会封装一个FrameLayout作为副容器,并将toolbar添加到第一位
 */
public abstract class ToolBarExpandActivity extends BaseActivity {

    private ToolBarHelper mToolBarHelper ;
    protected Toolbar toolbar ;

    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
        setContentView(mToolBarHelper.getContentView());
        initToolBar(toolbar);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止Context泄露
        if (null != mToolBarHelper) mToolBarHelper.clear();
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setTitle(title);
    }
}
