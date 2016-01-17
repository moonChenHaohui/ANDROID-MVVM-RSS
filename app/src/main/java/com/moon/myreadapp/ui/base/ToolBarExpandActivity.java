package com.moon.myreadapp.ui.base;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.scroll.ObservableScrollViewCallbacks;
import com.moon.myreadapp.common.components.scroll.ScrollState;
import com.moon.myreadapp.common.components.scroll.Scrollable;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by moon on 15/10/19.
 *
 */
public abstract class ToolBarExpandActivity<S extends Scrollable> extends BaseActivity implements ObservableScrollViewCallbacks {

    protected Toolbar mToolbar ;
    private S mScrollable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        setContentView(getLayoutView());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(mToolbar);
        mScrollable = createScrollable();
        mScrollable.setScrollViewCallbacks(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }


    protected abstract S createScrollable();

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    protected void showToolbar() {
        moveToolbar(0);
    }

    protected void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY((View) mScrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mScrollable).requestLayout();
            }
        });
        animator.start();
    }
}
