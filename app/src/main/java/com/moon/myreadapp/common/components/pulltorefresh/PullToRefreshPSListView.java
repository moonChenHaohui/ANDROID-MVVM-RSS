package com.moon.myreadapp.common.components.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.moon.myreadapp.common.components.pinnedsectionlistView.PinnedSectionListView;
import com.moon.myreadapp.common.components.pulltorefresh.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshPSListView extends PullToRefreshListView{


    public PullToRefreshPSListView(Context context) {
        super(context);
    }

    public PullToRefreshPSListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshPSListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView listView = new PinnedSectionListView(context,attrs);
        mListView = listView;
        mListView.setOnScrollListener(this);
        //mRecyclerView.setFooterDividersEnabled(false);
        mListView.setDivider(null);
        return listView;
    }
}
