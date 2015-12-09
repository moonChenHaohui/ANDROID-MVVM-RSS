package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.moon.appframework.action.EventAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshPSListView;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityFeedBinding;
import com.moon.myreadapp.mvvm.viewmodels.FeedViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

import de.halfbit.tinybus.Subscribe;


public class FeedActivity extends BaseActivity {


    @Override
    protected int getLayoutView() {
        return R.layout.activity_feed;
    }

    boolean isFastScroll = false;

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
                //binding.mainList.onPullDownRefreshComplete();
                binding.feedList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mainViewModel.getFeedRecAdapter().add(new Feed(null, "this is added raw", 2, "珠海", "no type", "http://www.baidu.com/", new Date(), "China", "2015 copy rights", "", "moon creater", 1), 0);
                        binding.feedList.onPullDownRefreshComplete();
                    }
                }, 3000);
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
        } else if (id == R.id.action_read_all) {
            //XDispatcher.from(this).dispatch(new EventAction(new AEvent("change from channel")));
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

    @Subscribe
    public void onEvent(UpdateArticleEvent event) {
        if (feedViewModel != null){
            feedViewModel.updateArticleUseCount(event);
        }
    }

}
