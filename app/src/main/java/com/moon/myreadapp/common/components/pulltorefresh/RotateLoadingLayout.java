package com.moon.myreadapp.common.components.pulltorefresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class RotateLoadingLayout extends LoadingLayout {
    /**旋转动画的时间*/
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /**动画插值*/
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /**Header的容器*/
    private RelativeLayout mHeaderContainer;
    /**箭头图片*/
    private ImageView mArrowImageView;
    /**刷新图片*/
    private ImageView mRefreshImageView;
    /**状态提示TextView*/
    private TextView mHintTextView;
    /**最后更新时间的TextView*/
    private TextView mHeaderTimeView;
    /**最后更新时间的标题*/
    private TextView mHeaderTimeViewTitle;
    /**旋转的动画*/
    private Animation mRotateAnimation;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public RotateLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public RotateLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     * 
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mRefreshImageView = (ImageView)findViewById(R.id.pull_to_refresh_header_refreshing);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        mHeaderTimeViewTitle.setVisibility(INVISIBLE);
        mArrowImageView.setScaleType(ScaleType.CENTER_INSIDE);
        //mArrowImageView.setImageResource(R.drawable.default_ptr_rotate);
        
        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = 720.0f;    // SUPPRESS CHECKSTYLE
        mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header2, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 如果最后更新的时间的文本是空的话，隐藏前面的标题
        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 60);
    }
    
    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        resetRotation();
        //在reset时显示arrow
        ObjectAnimator.ofFloat(mRefreshImageView, "alpha",0).start();
        ObjectAnimator.ofFloat(mArrowImageView, "alpha",1).start();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }
    
    @Override
    protected void onPullToRefresh() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }
    
    @Override
    protected void onRefreshing() {
        resetRotation();
        //在刷新时显示refreshview
        ObjectAnimator.ofFloat(mRefreshImageView, "alpha",1).start();
        ObjectAnimator.ofFloat(mArrowImageView, "alpha",0).start();
        mRefreshImageView.startAnimation(mRotateAnimation);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    private static int beginAngle = 140;
    @Override
    public void onPull(float scale) {
        //当拉动一定程度才会转动图标
        float angle = scale * 180f; // SUPPRESS CHECKSTYLE
        //mArrowImageView.setRotation(angle);
        if (angle > beginAngle && angle <= 180) {
            mArrowImageView.setRotation((angle - beginAngle) / (180f - beginAngle) * 180f);
        } else if (angle > 180){
            mArrowImageView.setRotation(180);
        } else {
            mArrowImageView.setRotation(0);
        }
    }
    
    /**
     * 重置动画
     */
    private void resetRotation() {
        mRefreshImageView.clearAnimation();
        mRefreshImageView.setRotation(0);
        ObjectAnimator.ofFloat(mArrowImageView, "rotation",0).start();
    }
}
