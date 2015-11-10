package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.FeedRecAdapter;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.ChannelActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private IMainView mView;

    private FeedRecAdapter feedRecAdapter;

    private RecyclerItemClickListener readItemClickListener;

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
        final List<Feed> channels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            channels.add(new Feed(null,"在知乎上被吐槽是怎么样一种体验",2,"珠海","no type","http://www.baidu.com/",new Date(),"China","2015 copy rights","","moon creater",1));
        }
        feedRecAdapter = new FeedRecAdapter(channels);

        readItemClickListener = new RecyclerItemClickListener((Activity)mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                Feed feed = feedRecAdapter.getItem(position);
                Intent intent = new Intent();
                //intent.putExtra(Constants.CHANNEL_OBJ, feed);
                XDispatcher.from((Activity)mView).dispatch(new RouterAction(ChannelActivity.class,true));
                XLog.d("pos:" + position);
            }
        });



    }

    @Bindable
    public FeedRecAdapter getFeedRecAdapter() {
        return feedRecAdapter;
    }

    public void setFeedRecAdapter(FeedRecAdapter feedRecAdapter) {
        this.feedRecAdapter = feedRecAdapter;
        notifyPropertyChanged(BR.feedRecAdapter);
    }

    public RecyclerItemClickListener getReadItemClickListener() {
        return readItemClickListener;
    }

    public void setReadItemClickListener(RecyclerItemClickListener readItemClickListener) {
        this.readItemClickListener = readItemClickListener;
    }

    @Override
    public void clear() {
        mView = null;
    }

}
