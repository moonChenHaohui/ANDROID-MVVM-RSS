package com.moon.myreadapp.common.components.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by haohui.chh on 2015/8/14.
 */
public class PagerRecyclerView extends ObservableRecyclerView {

    public PagerRecyclerView(Context context) {
        super(context);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
    }


    private float xDistance, yDistance, xLast, yLast;

    private boolean mIsScrollUp = false;

    @Override
    public boolean canScrollVertically(int direction) {
        if (!isShown()) {
            return false;
        }
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // 放弃横向滚动
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                if (xDistance > yDistance) {
                    return false;
                }
                mIsScrollUp = curY - yLast > 0;
                break;
            case MotionEvent.ACTION_UP:
                xLast = ev.getX();
                yLast = ev.getY();
                mIsScrollUp = false;
                break;
        }

        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}
