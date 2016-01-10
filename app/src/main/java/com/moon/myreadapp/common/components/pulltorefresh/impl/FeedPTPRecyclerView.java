package com.moon.myreadapp.common.components.pulltorefresh.impl;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshRecyclerView;
import com.moon.myreadapp.databinding.WidgetEmptyHomeBinding;

/**
 * Created by moon on 16/1/9.
 * 主页 使用的 下拉刷新控件
 */
public class FeedPTPRecyclerView extends PullToRefreshRecyclerView{

    private WidgetEmptyHomeBinding emptyHomeBinding;

    public FeedPTPRecyclerView(Context context) {
        super(context);
    }

    public FeedPTPRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedPTPRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected View createEmptyLayout(Context context, AttributeSet attrs) {
        emptyHomeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_empty_home,null,false);
//        emptyHomeBinding.addFeed.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setShowEmptyLayout(false);
//                //刷新
//                doPullRefreshing(true,500);
//
//            }
//        });
        return emptyHomeBinding.getRoot();
    }
}
