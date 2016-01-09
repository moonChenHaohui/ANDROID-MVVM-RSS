package com.moon.myreadapp.common.components.pulltorefresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.common.components.pulltorefresh.ILoadingLayout.State;

import java.util.List;


/**
 * Created by moon on 15/11/07.
 * 实现了RecyclerView的下拉刷新 上拉加载
 * 添加了可以添加头部的的adapter
 * 添加了emptyview 对于数据的响应
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    /**
     * ListView
     */
    protected RecyclerView mRecyclerView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private RecyclerView.OnScrollListener mScrollListener;


    private BaseRecyclerAdapter mAdapter;

    /**
     * 布局管理器
     */
    private LinearLayoutManager mLinearLayoutManager;

    /**
     * 设置还剩x个item时进行load more
     */
    private int toEndSize = 3;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }

        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isScrollLoadEnabled() && hasMoreData()) {
                    if (null == mAdapter) {
                        return;
                    }
                    if (mAdapter.getItemCount() <= (mLinearLayoutManager
                            .findLastVisibleItemPosition() + 1 + toEndSize)
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (isReadyForPullUp()) {
                            startLoading();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //解决RecyclerView和SwipeRefreshLayout共用存在的bug
                setEnabled(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        };
        if (mRecyclerView != null) {
            mRecyclerView.setOnScrollListener(mScrollListener);
        }
    }



    public BaseRecyclerAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView listView = new RecyclerView(context){

            /**
             * 平滑滚动
             */
            Scroller scroller = new Scroller(getContext());
            @Override
            public void computeScroll() {
                if (scroller.computeScrollOffset()){
                    scrollTo(scroller.getCurrX(),scroller.getCurrY());
                    postInvalidate();
                }
            }
        };
        mRecyclerView = listView;

        //mAdapter = new ComAdapter(null);
        //mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setFooterDividersEnabled(false);
        //mRecyclerView.setDivider(null);
        return listView;
    }
    public void setAdapter(BaseRecyclerAdapter adapter){
        //mAdapter.setAdapter(adapter);
        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
        //将adapter的数据更新与emptyview绑定
        mAdapter.setNotify(bindEmptyView());
        //第一次需要判断是否需要显示
        if (!isPullRefreshing() && (mAdapter.getmData() == null || mAdapter.getmData().size() == 0)){
            setShowEmptyLayout(true);
        }
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
            }

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
    }


    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        if (isScrollLoadEnabled() == scrollLoadEnabled) {
            return;
        }

        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout && mAdapter != null) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
                mAdapter.addFooter(mLoadMoreFooterLayout);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }


    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new RotateLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }
        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        if (mLinearLayoutManager == null){
            return true;
        }

        Log.d("child","mLinearLayoutManager.findFirstCompletelyVisibleItemPosition():" + mLinearLayoutManager.findFirstCompletelyVisibleItemPosition());
        return mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {


        final int lastItemPosition = mAdapter.getItemCount() - 1;
        final int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses base_slide_remain FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mLinearLayoutManager.findFirstVisibleItemPosition();
            final int childCount = mRecyclerView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mRecyclerView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mRecyclerView.getBottom();
            }
        }

        return false;
    }


    /**
     * 用于绑定 emptyview and data
     */
    public BaseRecyclerAdapter.Notify bindEmptyView(){
        if (mNotify == null) {
            mNotify = new BaseRecyclerAdapter.Notify() {
                @Override
                public void onDataReSet(List data) {
                    if (data == null || data.size() == 0) {
                        setShowEmptyLayout(true);
                    } else {
                        setShowEmptyLayout(false);
                    }
                }
            };
        }
        return mNotify;

    }


    private BaseRecyclerAdapter.Notify mNotify;
}
