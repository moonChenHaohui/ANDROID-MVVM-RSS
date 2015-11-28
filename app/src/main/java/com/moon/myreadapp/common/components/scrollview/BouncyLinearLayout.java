package com.moon.myreadapp.common.components.scrollview;

/**
 * Created by moon on 15/10/24.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.util.ScreenUtils;

public class BouncyLinearLayout extends LinearLayout {

    private static final String TAG = "BouncyLinearLayout";
    private Scroller mScroller;
    private GestureDetector mGestureDetector;

    public BouncyLinearLayout(Context context) {
        this(context, null);
    }

    public BouncyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new BouncyGestureListener());
    }

    /**
     * 滚动到目标位置
     *
     * @param fx
     * @param fy
     */
    protected void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 设置滚动的相对偏移
     *
     * @param dx
     * @param dy
     */
    protected void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        //判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                actionUP();
                //XLog.d("scroll:getTop:" + getTop() + ",getBottom():" + getBottom() + ",getHeight():" + getHeight());
                return false;
            default:
                return mGestureDetector.onTouchEvent(event);
        }
        //return super.onTouchEvent(event);
    }

    /**
     * 回弹函数
     */
    private void actionUP() {
        if (getScrollY() < 0 || getHeight() > mTotalLength) // 顶部回弹
        {
            XLog.d("edsheng顶部回弹！！！！");
            smoothScrollTo(0, 0); // 开启回弹效果
        } else if (getScrollY() + getHeight() > mTotalLength) // 底部回弹
        {
            XLog.d("edsheng底部回弹！！！！");
            // 开启底部回弹
            smoothScrollTo(0, -(getHeight() - mTotalLength));
        }
    }

    private int mTotalLength;

    /***
     * 重写layout方法
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childStartPostion = 0;
        mTotalLength = 0;
        final int count = getChildCount();
        if (count == 0) {
            return;
        }
        childStartPostion = getPaddingTop();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != View.GONE) {
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                final int childHeight = child.getMeasuredHeight();
                int leftMargin = 0;
                int rightMargin = 0;
                int topMargin = 0;
                int bottomMargin = 0;
                if (lp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) lp;
                    leftMargin = mlp.leftMargin;
                    rightMargin = mlp.rightMargin;
                    topMargin = mlp.topMargin;
                    bottomMargin = mlp.bottomMargin;
                }

                childStartPostion += topMargin;
                int startX = (getWidth() - leftMargin - rightMargin - child
                        .getMeasuredWidth()) / 2 + leftMargin;
                child.layout(startX, childStartPostion,
                        startX + child.getMeasuredWidth(), childStartPostion
                                + childHeight);
                childStartPostion += (childHeight + bottomMargin);
            }
        }
        childStartPostion += getPaddingBottom();
        mTotalLength = childStartPostion;

    }

    class BouncyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            int dis = (int) ((distanceY - 0.5) / 2);
            smoothScrollBy(0, dis);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }

    }
}