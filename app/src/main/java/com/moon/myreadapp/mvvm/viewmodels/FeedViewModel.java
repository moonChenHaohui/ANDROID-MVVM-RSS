package com.moon.myreadapp.mvvm.viewmodels;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.router.XRouter;
import com.moon.appframework.common.router.ZRuler;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.common.adapter.ArticleRecAdapter;
import com.moon.myreadapp.common.adapter.FeedAdapter;
import com.moon.myreadapp.common.adapter.base.FeedAdapterHelper;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.Feed;
import com.moon.myreadapp.mvvm.models.ListFeed;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedViewModel extends BaseViewModel {

    private IView mView;

    private RecyclerItemClickListener articleClickListener;
    private ArticleRecAdapter mAdapter;
    private List<Article> articles;
    public FeedViewModel(IView view) {
        this.mView = view;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        mAdapter = new ArticleRecAdapter(DBHelper.Query.getArticles());
    }

    @Override
    public void initEvents() {
        articleClickListener = new RecyclerItemClickListener((Context)mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.ARTICLE_ID, mAdapter.getItem(position).getId());
                XDispatcher.from((Context)mView).dispatch(new RouterAction(ArticleActivity.class,bundle,true));
            }
        });
    }

    public ArticleRecAdapter getmAdapter() {
        return mAdapter;
    }

    public RecyclerItemClickListener getArticleClickListener() {
        return articleClickListener;
    }

    @Override
    public void clear() {
        mView = null;
    }
}
