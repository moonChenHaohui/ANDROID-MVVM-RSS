package com.moon.myreadapp.ui.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XActivity;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.swipeback.SwipeBackLayout;
import com.moon.myreadapp.common.event.UpdateUIEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ThemeUtils;

import de.halfbit.tinybus.Subscribe;


/**
 * Created by moon on 15/10/18.
 *
 *
 *
 * 附带实现了左滑finish activity
 * --来自瑞克大神blog:http://blog.csdn.net/xiaanming/article/details/20934541:
 * --注意点:activity需要设置主题透明:
 * <p>
 * <item name="android:windowBackground">@color/transparent</item>
 * 　　<item name="android:windowIsTranslucent">true</item>
 * 　　<item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item>
 * </p>
 * 改动了一下生效区域,在
 * SwipeBackLayout.SCROLL_SIZE ;
 * SwipeBackLayout.ATREA_PERCETAGE;
 * 增加了对栈底activity的判断,使不会直接滑退app
 */
public abstract class Base1Activity extends XActivity{


    protected SwipeBackLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //配置主题
        initTheme();
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.activity_base, null);
        layout.attachToActivity(this);
        //event bus init
        XDispatcher.register(this);
        setContentViewAndBindVm(savedInstanceState);
    }
    abstract void setContentViewAndBindVm(Bundle savedInstanceState);
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        injectView(this);
    }

    private void initTheme() {
        ThemeUtils.Theme theme = ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changTheme(this, theme);
    }

    /**
     * 该方法提供统一设置toolbar的方式;  可以继承 ToolBarExpandActivity,可以直接再顶部视图中设置一个toolbar
     *
     * @param toolbar
     */
    protected void initToolBar(Toolbar toolbar) {
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        toolbar.collapseActionView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XDispatcher.unregister(this);
    }

    private void showActivityInAnim() {
       // overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    private void showActivityExitAnim() {
       // overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        //showActivityExitAnim();
    }

    protected abstract Toolbar getToolBar();

    protected abstract
    @LayoutRes
    int getLayoutView();

    @Subscribe
    public void onUpdateEvent(UpdateUIEvent event) {
        if (event.getStatus() == UpdateUIEvent.THEME_CHANGE) {
            recreate();
        }
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

}
