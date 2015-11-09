package com.moon.myreadapp.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.moon.appframework.action.EventAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshPSListView;
import com.moon.myreadapp.mvvm.viewmodels.ChannelViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;


public class ChannelActivity extends BaseActivity {


    @Override
    protected int getLayoutView() {
        return R.layout.activity_feed;
    }

    boolean isFastScroll = false;
    private PullToRefreshPSListView listView;
    private Toolbar toolbar;

    private ChannelViewModel channelViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        initToolBar(toolbar);
        channelViewModel = new ChannelViewModel(this);
        listView = (PullToRefreshPSListView)findViewById(R.id.channel_list);
        listView.getRefreshableView().setFastScrollEnabled(true);
        listView.getRefreshableView().setSmoothScrollbarEnabled(true);
        listView.getRefreshableView().setVerticalScrollBarEnabled(false);
        listView.getRefreshableView().setOnItemClickListener(channelViewModel.getFeedItemClickListener());
        listView.setPullLoadEnabled(true);
        listView.setScrollLoadEnabled(true);
        listView.getRefreshableView().setAdapter(channelViewModel.getFeedAdapter());
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                listView.onPullDownRefreshComplete();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                listView.onPullUpRefreshComplete();
            }
        });
    }

    public PullToRefreshPSListView getListView() {
        return listView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.content) {
            finish();
        } else if (id == R.id.action_read_all) {
            XDispatcher.from(this).dispatch(new EventAction(new AEvent("change from channel")));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        setContentView(getLayoutView());
    }



}
