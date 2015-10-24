package com.moon.myreadapp.ui.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.moon.appframework.core.XActivity;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.swipeback.SwipeBackLayout;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.ThemeUtils;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;


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
public abstract class BaseActivity extends XActivity implements IView{




    public static void start(Activity context, Class clazz) {
        Intent intent = new Intent();
        start(context, clazz, intent);
    }

    public static void start(Activity context, Class clazz, Intent intent) {
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }

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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
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
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    private void showActivityExitAnim() {
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        showActivityExitAnim();
    }

    protected abstract Toolbar getTooBar();

    protected abstract
    @LayoutRes
    int getLayoutView();


    protected void showToolbar(Toolbar toolbar) {
        moveToolbar(toolbar,0);
    }

    protected void hideToolbar(Toolbar toolbar) {
        moveToolbar(toolbar,-toolbar.getHeight());
    }

    private void moveToolbar(final Toolbar toolbar,float toTranslationY) {
        if (ViewHelper.getTranslationY(toolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbar, translationY);
                ViewHelper.setTranslationY((View) layout, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) layout).getLayoutParams();
                lp.height = (int) -translationY + findViewById(android.R.id.content).getHeight() - lp.topMargin;
                ((View) layout).requestLayout();
            }
        });
        animator.start();
    }
}
