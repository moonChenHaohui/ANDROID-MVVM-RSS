package com.moon.myreadapp.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.moon.appframework.action.EventAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
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
        toolbar = (Toolbar)findViewById(R.id.toolbar);
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
        binding = DataBindingUtil.setContentView(this, getLayoutView());

        this.drawerViewModel = new DrawerViewModel(this);
        binding.setDrawerViewModel(drawerViewModel);

        this.mainViewModel = new MainViewModel(this);
        binding.setMainViewModel(mainViewModel);
    }

    private void initBusiness() {
        initDrawer();
        initMainView();
    }
    private void initDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, 0, 0) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                toolbar.setTitle("open");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                toolbar.setTitle("close");
            }
        };
        binding.drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initMainView(){
        binding.mainList.setPullLoadEnabled(true);
        binding.mainList.setScrollLoadEnabled(true);
        binding.mainList.getRefreshableView().addHeaderView(LayoutInflater.from(binding.mainList.getContext()).inflate(R.layout.lv_channel_header, null));
        binding.mainList.getRefreshableView().setOnItemClickListener(mainViewModel.getReadItemClickListener());

        binding.mainList.getRefreshableView().setAdapter(mainViewModel.getReadAdapter());
        binding.mainList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                binding.mainList.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                binding.mainList.onPullUpRefreshComplete();
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
        if(id == R.id.action_reflash){
        } else if (id == R.id.action_add){
            XDispatcher.from(this).dispatch(new EventAction(new AEvent("from main act")));
            XLog.d("post");
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onShakeEvent(AEvent event) {
        binding.leftDrawer.appInfo.setText(event.getA());
    }


    @Override
    public void onPullDownRefreshComplete() {
        binding.mainList.onPullDownRefreshComplete();
    }

    @Override
    public void onPullUpRefreshComplete() {
        binding.mainList.onPullUpRefreshComplete();
    }

}
