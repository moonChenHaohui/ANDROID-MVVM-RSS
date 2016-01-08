package com.moon.myreadapp.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.common.event.UpdateUserEvent;
import com.moon.myreadapp.databinding.ActivityHomeBinding;
import com.moon.myreadapp.mvvm.viewmodels.DrawerViewModel;
import com.moon.myreadapp.mvvm.viewmodels.MainViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;

import de.halfbit.tinybus.Subscribe;


public class MainActivity extends BaseActivity implements IMainView {

    private Toolbar toolbar;


    private ActionBarDrawerToggle mDrawerToggle;


    ActivityHomeBinding binding;

    private DrawerViewModel drawerViewModel;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);
        initBusiness();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initToolBar(Toolbar toolbar) {
        super.initToolBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_dialog_dialer);
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(this),getLayoutView(),null,false);

        setContentView(binding.getRoot());

        this.drawerViewModel = new DrawerViewModel(this);
        binding.setDrawerViewModel(drawerViewModel);

        this.mainViewModel = new MainViewModel(this);
        binding.setMainViewModel(mainViewModel);
    }

    private void initBusiness() {
        initDrawer();
        initMainView();
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, 0,0) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                //toolbar.setTitle("open");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                //toolbar.setTitle("close");
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //切换的效果
                int dis = (int)(slideOffset * getScreenWidth() / 3.0);
                binding.mainContent.scrollTo(-dis,0);
            }
        };
        binding.drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initMainView() {

        //必须先设置了adapter,才能进行add head\footer,设置刷新等等操作.
        binding.mainList.setAdapter(mainViewModel.getFeedRecAdapter());
        //binding.mainList.getmAdapter().addHeader(LayoutInflater.from(binding.mainList.getContext()).inflate(R.layout.lv_feed_header, null));
//        binding.mainList.setPullLoadEnabled(false);
//        binding.mainList.setScrollLoadEnabled(false);
//        binding.mainList.setPullRefreshEnabled(false);
        binding.mainList.setHasMoreData(false);
        binding.mainList.getRefreshableView().addOnItemTouchListener(mainViewModel.getReadItemClickListener());
        binding.mainList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //下拉刷新
                //binding.mainList.onPullDownRefreshComplete();
                binding.mainList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mainViewModel.getFeedRecAdapter().add(new Feed(null, "this is added raw", 2, "珠海", "no type", "http://www.baidu.com/", new Date(), "China", "2015 copy rights", "", "moon creater", 1), 0);
                        binding.mainList.onPullDownRefreshComplete();
                    }
                }, 3000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //上拉加载
                //binding.mainList.onPullUpRefreshComplete();
                //binding.mainList.setShowEmptyLayout(true);
                binding.mainList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // mainViewModel.getFeedRecAdapter().add(new Feed(null, "this is added raw", 2, "珠海", "no type", "http://www.baidu.com/", new Date(), "China", "2015 copy rights", "", "moon creater", 1));
                        binding.mainList.onPullUpRefreshComplete();
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_home;
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mainViewModel.refreshAll();
        } else if (id == R.id.action_add) {
            mainViewModel.onAddButtonClick();
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onUpdateEvent(UpdateFeedEvent event) {
        //XLog.d("get mes:" + "UpdateFeedEvent");
        mainViewModel.updateFeed(event);
    }

    @Subscribe
    public void onUpdateEvent(UpdateFeedListEvent event) {
        mainViewModel.updateFeeds();
    }

    @Subscribe
    public void onUpdateEvent(UpdateUserEvent event) {
        XLog.d("get mes:" + "UpdateUserEvent");
        drawerViewModel.updateUser(event.getUser());
    }

    public void btnOnClick(View v) {
        mainViewModel.btnOnClick(v);

    }
    @Override
    public void onPullDownRefreshComplete() {
        binding.mainList.onPullDownRefreshComplete();
    }

    @Override
    public void onPullUpRefreshComplete() {
        binding.mainList.onPullUpRefreshComplete();
    }

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

}
