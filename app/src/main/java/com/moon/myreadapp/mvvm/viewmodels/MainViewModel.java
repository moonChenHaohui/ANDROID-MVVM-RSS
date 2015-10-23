package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.common.adapter.ReadAdapter;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.Channel;
import com.moon.myreadapp.ui.ChannelActivity;
import com.moon.myreadapp.ui.MainActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private IMainView mView;

    private ReadAdapter readAdapter;

    private AdapterView.OnItemClickListener readItemClickListener;

    public MainViewModel(IMainView view) {
        this.mView = view;
        //initViews();
        initEvents();
    }


    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
        final List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            channels.add(new Channel("s" + i,1));
        }
        readAdapter = new ReadAdapter(channels);

        readItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      Channel channel = readAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.CHANNEL_OBJ,channel);
                MainActivity.start((Activity) mView, ChannelActivity.class);
                XLog.d("pos:"+position);
            }
        };
    }

    @Bindable
    public ReadAdapter getReadAdapter() {
        return readAdapter;
    }

    public void setReadAdapter(ReadAdapter readAdapter) {
        this.readAdapter = readAdapter;
    }

    public AdapterView.OnItemClickListener getReadItemClickListener() {
        return readItemClickListener;
    }

    public void setReadItemClickListener(AdapterView.OnItemClickListener readItemClickListener) {
        this.readItemClickListener = readItemClickListener;
    }

    @Override
    public void clear() {
        mView = null;
    }

}
