package com.moon.myreadapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moon.appframework.action.EventAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshScrollView;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.ui.base.ToolBarExpandActivity;

public class FeedActivity extends BaseActivity {


    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        PullToRefreshScrollView mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.feed_body);
        mPullRefreshScrollView.setPullLoadEnabled(true);
        mPullRefreshScrollView.setScrollLoadEnabled(true);
        mPullRefreshScrollView.getFooterLoadingLayout().setTXTpullLabelText(Html.fromHtml("放开后加载下篇:<font color=\"#FF5500\">xxxxxxxxxxxxxxxxxxx</font>"));
        mPullRefreshScrollView.getFooterLoadingLayout().setTXTreleaseToRefreshText(Html.fromHtml("加载下篇:<font color=\"#FF5500\">xxxxxxxxxxxxxxxxxxx</font>"));
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        ScrollView mScrollView = mPullRefreshScrollView.getRefreshableView();

        mScrollView.addView(LayoutInflater.from(this).inflate(R.layout.feed_body,null));
    }

    @Override
    protected Toolbar getTooBar() {
        return toolbar;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_feed;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        setContentView(getLayoutView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.content) {
            finish();
        } else if (id == R.id.action_read_all) {
            hideToolbar(toolbar);
            XDispatcher.from(this).dispatch(new EventAction(new AEvent("change from channel")));
        }
        return super.onOptionsItemSelected(item);
    }
}
