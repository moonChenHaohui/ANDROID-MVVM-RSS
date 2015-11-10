package com.moon.myreadapp.mvvm.viewmodels;

import android.view.View;
import android.widget.AdapterView;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.common.adapter.FeedAdapter;
import com.moon.myreadapp.common.adapter.base.FeedAdapterHelper;
import com.moon.myreadapp.mvvm.models.Feed;
import com.moon.myreadapp.mvvm.models.ListFeed;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.ui.base.IViews.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedViewModel extends BaseViewModel {

    private IView mView;

    private AdapterView.OnItemClickListener feedItemClickListener;
    private FeedAdapter feedAdapter;
    private List<ListFeed> feeds;
    public FeedViewModel(IView view) {
        this.mView = view;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        feeds = new ArrayList<>();
        final int sectionsNumber = 3;
        int sectionPosition = 0, listPosition = 0;
        for (char i = 0; i < sectionsNumber; i++) {
            ListFeed section = new ListFeed(new Feed());
            feeds.add(section);

            final int itemsNumber = (int) Math.abs((Math.cos(2f * Math.PI / 3f * sectionsNumber / (i + 1f)) * 25f));
            for (int j = 0; j < itemsNumber; j++) {
                ListFeed item = new ListFeed(new Feed("2"));

                item.sectionPosition = sectionPosition;
                item.listPosition = listPosition++;
                feeds.add(item);
            }

            sectionPosition++;
        }
        feedAdapter = new FeedAdapter(feeds);
    }

    @Override
    public void initEvents() {
        feedItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (feeds.get(position).tpye == FeedAdapterHelper.TimeType.TODATY){
                    XLog.d("is a title");

                    //TODO 这个滚动应该放在view中,去调用

                    if (android.os.Build.VERSION.SDK_INT >= 8) {
                        ((FeedActivity)mView).getListView().getRefreshableView().smoothScrollToPosition(position);
                    } else {
                        ((FeedActivity)mView).getListView().getRefreshableView().setSelection(position);
                    }
                } else {
                    XLog.d("is not a title");
                    XDispatcher.from((FeedActivity)mView).dispatch(new RouterAction(ArticleActivity.class,true));
                }
            }
        };
    }

    public AdapterView.OnItemClickListener getFeedItemClickListener() {
        return feedItemClickListener;
    }

    public void setFeedItemClickListener(AdapterView.OnItemClickListener feedItemClickListener) {
        this.feedItemClickListener = feedItemClickListener;
    }

    public FeedAdapter getFeedAdapter() {
        return feedAdapter;
    }

    @Override
    public void clear() {
        mView = null;
    }
}
