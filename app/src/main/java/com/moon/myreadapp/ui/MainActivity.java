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
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.action.EventAction;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.databinding.ActivityHomeBinding;
import com.moon.myreadapp.mvvm.viewmodels.DrawerViewModel;
import com.moon.myreadapp.mvvm.viewmodels.MainViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;

import java.util.ArrayList;

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
        binding.mainList.setPullLoadEnabled(false);
        binding.mainList.setScrollLoadEnabled(true);
       // binding.mainList.getRefreshableView().addHeaderView(LayoutInflater.from(binding.mainList.getContext()).inflate(R.layout.lv_channel_header, null));
        //binding.mainList.getRefreshableView().setOnClickListener(mainViewModel.getReadItemClickListener());
        binding.mainList.getRefreshableView().addOnItemTouchListener(mainViewModel.getReadItemClickListener());
        ArrayList<String> data = new ArrayList<String>(){{add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");add("sss");}};
        binding.mainList.setAdapter(new MyAdapter((data)));
        binding.mainList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //下拉刷新
                binding.mainList.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //上拉加载
               binding.mainList.onPullUpRefreshComplete();
            }
        });


        binding.leftDrawer.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDispatcher.from(MainActivity.this).dispatch(new RouterAction(SettingActivity.class,true));
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

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public ArrayList<String> datas = null;
        public MyAdapter(ArrayList<String> datas) {
            this.datas = datas;
        }
        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_channel_item,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(datas.get(position));
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public IconTextView mTextView;
            public ViewHolder(View view){
                super(view);
                mTextView = (IconTextView) view.findViewById(R.id.channel_name);
            }
        }
    }
}
