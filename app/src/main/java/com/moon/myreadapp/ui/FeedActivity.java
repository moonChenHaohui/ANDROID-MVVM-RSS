package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.event.UpdateUIEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityFeedBinding;
import com.moon.myreadapp.mvvm.viewmodels.FeedViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ThemeUtils;


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
                //binding.mainList.onPullUpRefreshComplete();
                //binding.mainList.setShowEmptyLayout(true);
                binding.feedList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // mainViewModel.getFeedRecAdapter().add(new Feed(null, "this is added raw", 2, "珠海", "no type", "http://www.baidu.com/", new Date(), "China", "2015 copy rights", "", "moon creater", 1));
                        binding.feedList.onPullUpRefreshComplete();
                    }
                }, 3000);
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
        } else if (id == R.id.action_refresh){
            XLog.d("onOptionsItemSelected:");
            PreferenceUtils.getInstance(this)
                    .saveParam(this.getString(R.string.set_theme), ThemeUtils.Theme.YELLOW.getIntValue());
            XApplication.getInstance().bus.post(new UpdateUIEvent(UpdateUIEvent.THEME_CHANGE));
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


}
