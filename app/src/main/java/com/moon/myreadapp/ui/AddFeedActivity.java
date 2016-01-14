package com.moon.myreadapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.AddSubViewPagerAdapter;
import com.moon.myreadapp.databinding.ActivityAddFeedBinding;
import com.moon.myreadapp.mvvm.viewmodels.AddFeedViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.ui.base.ToolBarExpandActivity;
import com.moon.myreadapp.ui.fragments.OPMLFragment;
import com.moon.myreadapp.ui.fragments.PrefFragment;
import com.moon.myreadapp.ui.fragments.RecommendFragment;
import com.moon.myreadapp.util.DialogFractory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddFeedActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActivityAddFeedBinding binding;
    private AddFeedViewModel addFeedViewModel;


    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_add_feed;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());
        addFeedViewModel = new AddFeedViewModel(this);
        binding.setModel(addFeedViewModel);

        binding.viewpager.setAdapter(addFeedViewModel.getAdapter());//给ViewPager设置适配器
        binding.tablayout.setupWithViewPager(binding.viewpager);//将TabLayout和ViewPager关联起来。
        binding.tablayout.setTabsFromPagerAdapter(addFeedViewModel.getAdapter());//给Tabs设置适配器
        binding.tablayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onDestroy() {
        if (addFeedViewModel != null ){
            addFeedViewModel.clear();
        }
        super.onDestroy();
    }

}
