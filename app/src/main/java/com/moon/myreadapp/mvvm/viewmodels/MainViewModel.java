package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.FeedRecAdapter;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;

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
        final List<Feed> feeds = DBHelper.Query.getFeeds();
        feedRecAdapter = new FeedRecAdapter(feeds);

        readItemClickListener = new RecyclerItemClickListener((Activity)mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                int pos = position - 1;
                Feed feed = feedRecAdapter.getItem(pos);
                if (feed == null )return;

                Bundle bundle = new Bundle();
                XLog.d("bundle" + bundle);
                bundle.putLong(Constants.FEED_ID, feed.getId());
                XDispatcher.from((Activity)mView).dispatch(new RouterAction(FeedActivity.class,bundle,true));
                XLog.d("pos:" + pos);
            }
        });
    }

    public void updateFeeds(){
        if (feedRecAdapter != null){
            feedRecAdapter.setmData(DBHelper.Query.getFeeds());
        }
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

    public void onAddButtonClick(){
        DialogFractory.create((Activity) mView, DialogFractory.Type.AddSubscrible).show();
    }
    @Override
    public void clear() {
        mView = null;
    }

}
