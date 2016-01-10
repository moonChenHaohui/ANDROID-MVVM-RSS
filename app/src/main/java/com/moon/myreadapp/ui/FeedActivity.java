package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.dialog.FeedSetDialog;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.common.event.UpdateUIEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityFeedBinding;
import com.moon.myreadapp.mvvm.viewmodels.FeedViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.util.Conver;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ThemeUtils;

import java.util.Date;

import de.halfbit.tinybus.Subscribe;


public class FeedActivity extends BaseActivity {


    @Override
    protected int getLayoutView() {
        return R.layout.activity_feed;
    }

    private Toolbar toolbar;

    private ActivityFeedBinding binding;

    private FeedViewModel feedViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        initToolBar(toolbar);
        initBusiness();
    }

    private void initBusiness (){
        binding.feedList.setAdapter(feedViewModel.getmAdapter());
        binding.feedList.setPullLoadEnabled(false);
        binding.feedList.setScrollLoadEnabled(true);
        binding.feedList.getHeaderLoadingLayout().setLastUpdatedLabel(Conver.ConverToString(new Date(), "HH:mm"));
        binding.feedList.getRefreshableView().addOnItemTouchListener(feedViewModel.getArticleClickListener());
        binding.feedList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //下拉刷新
                feedViewModel.refresh(binding.feedList);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //上拉加载
                binding.feedList.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean hasMoreData = feedViewModel.loadMore();
                        binding.feedList.onPullUpRefreshComplete();
                        binding.feedList.setHasMoreData(hasMoreData);
                    }
                });

            }
        });
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
        } else if (id == R.id.action_settings){
            //频道设置
            new FeedSetDialog(this).showWithView(binding.feedList);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());
        feedViewModel = new FeedViewModel(this,getIntent().getExtras().getLong(Constants.FEED_ID,-1));
        binding.setFeedViewModel(feedViewModel);
    }


    public void btnOnClick(View v) {
        feedViewModel.btnOnClick(v);
    }

    @Subscribe
    public void onUpdateEvent(UpdateFeedEvent event) {
        if (event.getType() == UpdateFeedEvent.TYPE.SET){
            feedViewModel.updateSet(event.isShowAllArticles());
        } else if (event.getType() == UpdateFeedEvent.TYPE.STATUS){
            //更新单个article 状态
            if (event.getUpdatePosition() >= 0) {
                feedViewModel.updateArticleByPosition(event.getUpdatePosition(),event.getArticle());
            }
        }

    }

}
