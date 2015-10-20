package com.moon.myreadapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.moon.myreadapp.common.adapter.DrawerAdapter;
import com.moon.myreadapp.databinding.LeftDrawerContentBinding;
import com.moon.myreadapp.mvvm.models.BaseMenuItem;
import com.moon.myreadapp.mvvm.viewmodels.DrawerViewModel;
import com.moon.myreadapp.mvvm.viewmodels.MainViewModel;
import com.moon.myreadapp.ui.BaseActivity;
import com.moon.myreadapp.ui.IMainView;
import com.moon.myreadapp.util.ToastUtil;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements IMainView{


    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.left_drawer)
    View mDrawer;
    @Bind(R.id.left_drawer_listview)
    ListView mDrawerListView;

    @Bind(R.id.toolbar)
    Toolbar toolbar ;

    /**
     * toggle
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerViewModel drawerViewModel;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(toolbar);
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewModels() {
        drawerViewModel = new DrawerViewModel(this,
                LeftDrawerContentBinding.bind(mDrawer));
        mainViewModel = new MainViewModel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void removeViewModels() {
        drawerViewModel.clear();
        mainViewModel.clear();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
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
    LoadToast lt;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        int id = item.getItemId();
        if (id == R.id.action_search){
            final LoadToast lt = ToastUtil.createToast(this,"hellow").show();
            mDrawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                  lt.success();
                }
            },3000);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initDrawer(List<BaseMenuItem> menus) {
        DrawerAdapter adapter = new DrawerAdapter(this,menus);
        mDrawerListView.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,0,0) {
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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
}
