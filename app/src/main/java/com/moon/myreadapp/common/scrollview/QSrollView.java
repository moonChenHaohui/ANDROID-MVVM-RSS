package com.moon.myreadapp.common.scrollview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;


/*
 * @FileName：QSrollView.java
 * @Version：V1.0
 * @Date: 2015-2-1 Create
 * @author: edsheng
 * */
class QSrollView extends ViewGroup {
    public final static String TAG = QSrollView.class.getSimpleName();
    public final static int TOUCH_STATE_SROLLING = 1; // 当前在滑动状态
    public final static int TOUCH_STATE_FLING = 2; // 当前fling状态
    public final static int TOUCH_STATE_DEFALUT = 0; // 默认

    private int mTouchState = TOUCH_STATE_DEFALUT;
    private int mTouchSlop = 0; // 当前滑动阀值

    private int mLastMontionY; // 记录上次y的位置

    Scroller mScroller; // 滑动辅助类

    private int mTotalLength = 0; // 整个控件的长度
    private int mMaxmumVelocity = 0; // Velocity的阀值
    private VelocityTracker mVelocityTracker; // Velocity

    int mPointID = 0; // pointID

    public QSrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.getTouchSlop();
        mMaxmumVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    @Override
    public void scrollBy(int x, int y) {
        // 判断当前视图是否超过了顶部或者顶部就让它滑动的距离为1/3这样就有越拉越拉不动的效果
        if (getScrollY() < 0 || getScrollY() + getHeight() > mTotalLength) {
            super.scrollBy(x, y / 3);
        } else {
            super.scrollBy(x, y);
        }
    }

    /**
     * 事件拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        // 表示已经开始滑动了，不需要走该Action_MOVE方法了(第一次时可能调用)。
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_DEFALUT)) {
            return true;
        }
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMontionY - y);
                // 超过了最小滑动距离
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SROLLING;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointID = ev.getPointerId(ev.getActionIndex()); // 记录当前pointID
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMontionY = y;
                //Log.e(TAG, mScroller.isFinished() + "");
                if (!mScroller.isFinished()) // 当动画还没有结束的时候强制结束
                {
                    mScroller.abortAnimation();
                    mScroller.forceFinished(true);
                }
                mTouchState = TOUCH_STATE_DEFALUT;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_DEFALUT;
                break;
        }
        //Log.e(TAG, mTouchState + "====" + TOUCH_STATE_DEFALUT);
        return mTouchState != TOUCH_STATE_DEFALUT;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int touchIndex = event.getActionIndex();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointID = event.getPointerId(0);
                mLastMontionY = (int) event.getY();// 记录按下的点
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 添加多点触控的处理
                mPointID = event.getPointerId(touchIndex);
                mLastMontionY = (int) (event.getY(touchIndex) + 0.5f); // 记录按下的点
                break;

            case MotionEvent.ACTION_MOVE:
                touchIndex = event.findPointerIndex(mPointID);
                if (touchIndex < 0) // 当前index小于0就返false继续接受下一次事件
                    return false;
                int detaY = (int) (mLastMontionY - event.getY(touchIndex)); // 计算滑动的距离
                scrollBy(0, detaY); // 调用滑动函数
                mLastMontionY = (int) event.getY(touchIndex); // 记录上一次按下的点
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("edsheng", "Action UP");
                mVelocityTracker.computeCurrentVelocity(1000);
                if (Math.abs(mVelocityTracker.getYVelocity()) > mMaxmumVelocity && !checkIsBroad()) {
                    mScroller.fling(getScrollX(), getScrollY(), 0, -(int) mVelocityTracker.getYVelocity(), 0, 0, 0,
                            mTotalLength - getHeight());
                } else {
                    actionUP(); // 回弹效果
                }

                mTouchState = TOUCH_STATE_DEFALUT;

                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 添加多点触控的支持
                if (event.getPointerId(touchIndex) == mPointID) {
                    final int newIndex = touchIndex == 0 ? 1 : 0;
                    mPointID = event.getPointerId(newIndex);
                    mLastMontionY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_DEFALUT;
                break;
            default:
                break;
        }
        // super.onTouchEvent(event);
        return true;
    }

    /**
     * 回弹函数
     */
    private void actionUP() {
        if (getScrollY() < 0 || getHeight() > mTotalLength) // 顶部回弹
        {
            //Log.d("edsheng", "顶部回弹！！！！");
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY()); // 开启回弹效果
            invalidate();
        } else if (getScrollY() + getHeight() > mTotalLength) // 底部回弹
        {
            // 开启底部回弹
            mScroller.startScroll(0, getScrollY(), 0, -(getScrollY()
                    + getHeight() - mTotalLength));
            invalidate();
        }
    }

    /***
     * 检测当前是否可回弹
     *
     * @return
     */
    boolean checkIsBroad() {
        if (getScrollY() < 0 || getScrollY() + getHeight() > mTotalLength) // 顶部回弹)
            // //顶部回弹
            return true;
        else
            return false;
    }

    /**
     * 重写onMeasure方法计算
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = getChildCount();
        final int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int paretnHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams childLp = child.getLayoutParams();
                final boolean childWidthWC = childLp.width == LayoutParams.WRAP_CONTENT;
                final boolean childHeightWC = childLp.height == LayoutParams.WRAP_CONTENT;
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams childMarginLp = (MarginLayoutParams) childLp;
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec
                            .makeMeasureSpec(parentWidthSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight()
                                    + childMarginLp.leftMargin
                                    + childMarginLp.rightMargin,
                            childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec
                            .makeMeasureSpec(paretnHeightSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom()
                                    + childMarginLp.topMargin
                                    + childMarginLp.bottomMargin,
                            childMarginLp.height);
                } else {
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec
                            .makeMeasureSpec(parentWidthSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight(),
                            childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec
                            .makeMeasureSpec(paretnHeightSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom(),
                            childLp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

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
                LayoutParams lp = child.getLayoutParams();
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

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) // 计算当前位置
        {
            // 滚动
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}