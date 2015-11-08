package com.moon.myreadapp.common.components.pulltorefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.moon.myreadapp.common.components.pulltorefresh.ILoadingLayout.State;

import java.util.ArrayList;


/**
 * Created by moon on 15/11/07.
 * 实现了RecyclerView的下拉刷新 上拉加载
 * 添加了可以添加头部的的adapter
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

    /**
     * 添加了head 和footer的 recycler adapter
     */
    private ComAdapter mAdapter;

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



    public ComAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView listView = new RecyclerView(context);
        mRecyclerView = listView;
        mAdapter = new ComAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setFooterDividersEnabled(false);
        //mRecyclerView.setDivider(null);
        return listView;
    }
    public void setAdapter(RecyclerView.Adapter adapter){
        mAdapter.setAdapter(adapter);
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
            if (null == mLoadMoreFooterLayout) {
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
     * 实现了头部和尾部的adapter
     */
    public static class ComAdapter extends RecyclerView.Adapter {

        private ArrayList<View> mHeadViews;
        private ArrayList<View> mFooterViews;
        private RecyclerView.Adapter mAdapter;

        final static int TYPE_HEAD = 1 << 10;
        final static int TYPE_FOOT = 1 << 11;

        private int headerPosition = 0;
        private int footerPosition = 0;

        public ComAdapter(RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;


        }

        public void addFooter(View view) {
            if (null == mFooterViews) {
                mFooterViews = new ArrayList<>();
            }
            mFooterViews.add(view);
        }


        public void addHeader(View view) {
            if (null == mHeadViews) {
                mHeadViews = new ArrayList<>();
            }
            mHeadViews.add(view);
        }

        public void setAdapter(RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        private int getHeaderSize() {
            return null == mHeadViews ? 0 : mHeadViews.size();
        }

        private int getFooterSize() {
            return null == mFooterViews ? 0 : mFooterViews.size();
        }

        /**
         * 判断是否是头部
         *
         * @param pos
         * @return
         */
        private boolean isHeader(int pos) {
            if (null == mHeadViews) return false;
            return pos >= 0 && pos < getHeaderSize();
        }

        /**
         * 判断是否是底部
         *
         * @param position
         * @return
         */
        public boolean isFooter(int position) {
            if (null == mFooterViews) return false;
            return position < getItemCount() && position >= getItemCount() - getFooterSize();
        }


        /**
         * 根据位置配置不同的view类型
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEAD;
            } else if (isFooter(position)) {
                return TYPE_FOOT;
            } else {
                int truePos = position - getHeaderSize();
                if (truePos >= 0 && truePos < mAdapter.getItemCount()) {
                    return mAdapter.getItemViewType(truePos);
                }
            }
            return RecyclerView.INVALID_TYPE;
        }

        @Override
        public int getItemCount() {
            return getHeaderSize() + getFooterSize() + (mAdapter == null ? 0 : mAdapter.getItemCount());
        }

        @Override
        public long getItemId(int position) {
            int truePos = position - getHeaderSize();
            if (mAdapter != null && truePos >= 0 && truePos < mAdapter.getItemCount()) {
                return mAdapter.getItemId(truePos);
            }
            return -1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int truePos = position - getHeaderSize();
            if (mAdapter != null && truePos >= 0 && truePos < mAdapter.getItemCount()) {
                mAdapter.onBindViewHolder(holder, truePos);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEAD) {
                return new OtherViewHolder(mHeadViews.get(headerPosition++));
            } else if (viewType == TYPE_FOOT) {
                return new OtherViewHolder(mFooterViews.get(footerPosition++));
            } else {
                return mAdapter.onCreateViewHolder(parent, viewType);
            }
        }


        private class OtherViewHolder extends RecyclerView.ViewHolder {

            public OtherViewHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
    }
}
