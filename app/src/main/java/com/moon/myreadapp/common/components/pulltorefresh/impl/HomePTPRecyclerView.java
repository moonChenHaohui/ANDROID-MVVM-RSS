package com.moon.myreadapp.common.components.pulltorefresh.impl;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.EmptyLayout;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshRecyclerView;
import com.moon.myreadapp.databinding.WidgetEmptyHomeBinding;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.ui.AddFeedActivity;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.util.DBHelper;

/**
 * Created by moon on 16/1/9.
 * 主页 使用的 下拉刷新控件
 */
public class HomePTPRecyclerView extends PullToRefreshRecyclerView implements View.OnClickListener {

    private WidgetEmptyHomeBinding emptyHomeBinding;

    public HomePTPRecyclerView(Context context) {
        super(context);
    }

    public HomePTPRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomePTPRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setShowEmptyLayout(boolean showEmptyLayout) {
        User user = DBHelper.Query.getUser();

        if (emptyHomeBinding != null)
            emptyHomeBinding.fucLogin.setVisibility(user == null ? VISIBLE : INVISIBLE);
        super.setShowEmptyLayout(showEmptyLayout);

    }


    @Override
    protected View createEmptyLayout(Context context, AttributeSet attrs) {
        emptyHomeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_empty_home, null, false);
        emptyHomeBinding.refresh.setOnClickListener(this);
        emptyHomeBinding.fucSubFeed.setOnClickListener(this);
        emptyHomeBinding.fucLogin.setOnClickListener(this);
        return emptyHomeBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.refresh:
                setShowEmptyLayout(false);
                //刷新
                doPullRefreshing(true, 100);
                break;
            case R.id.fuc_sub_feed:
                XDispatcher.from(getContext()).dispatch(new RouterAction(AddFeedActivity.class, true));
                break;
            case R.id.fuc_login:
                XDispatcher.from(getContext()).dispatch(new RouterAction(LoginActivity.class, true));
                break;
        }
    }

    public void setEmptyViewNotice(String notice) {
        if (emptyHomeBinding != null) {
            emptyHomeBinding.notice.setText(notice);
        }
    }
}
