package com.moon.myreadapp.ui;

import android.app.SearchManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.ActivityAddFeedBinding;
import com.moon.myreadapp.mvvm.viewmodels.AddFeedViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

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

        //init searchview
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //binding.searchView.setIconified(false);
        binding.searchView.setOnQueryTextListener(addFeedViewModel.getSearchListener());
        binding.result.setLayoutManager(new LinearLayoutManager(this));
        binding.result.setAdapter(addFeedViewModel.getSystemRecAdapter());
        addFeedViewModel.loadSystemData(binding.progress);
        
    }

    @Override
    protected void onDestroy() {
        if (addFeedViewModel != null ){
            addFeedViewModel.clear();
        }
        super.onDestroy();
    }

    public ActivityAddFeedBinding getBinding() {
        return binding;
    }
}
