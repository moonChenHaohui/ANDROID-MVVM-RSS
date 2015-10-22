package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.widget.ListView;

import com.moon.myreadapp.common.BaseViewModel;
import com.moon.myreadapp.common.adapter.ReadAdapter;
import com.moon.myreadapp.common.pulltorefresh.ui.PullToRefreshBase;
import com.moon.myreadapp.common.pulltorefresh.ui.PullToRefreshListView;
import com.moon.myreadapp.mvvm.models.Channel;
import com.moon.myreadapp.ui.base.IViews.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private IMainView mView;

    private ReadAdapter readAdapter;


    public MainViewModel(IMainView view) {
        this.mView = view;
        initViews();
        //initEvents();
    }


    @Override
    public void initViews() {
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            channels.add(new Channel("s" + i));
        }
        readAdapter = new ReadAdapter(channels);
    }

    @Override
    public void initEvents() {

    }

    @Bindable
    public ReadAdapter getReadAdapter() {
        return readAdapter;
    }

    public void setReadAdapter(ReadAdapter readAdapter) {
        this.readAdapter = readAdapter;
    }

    @Override
    public void clear() {
        mView = null;
    }

}
