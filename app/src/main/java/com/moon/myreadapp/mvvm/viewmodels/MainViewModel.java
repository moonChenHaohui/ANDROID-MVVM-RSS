package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XDispatcher;
import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.FeedRecAdapter;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.ui.MainActivity;
import com.moon.myreadapp.ui.helper.RefreshAsyncTask;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.StringHelper;
import com.moon.myreadapp.util.VibratorHelper;
import com.moon.myreadapp.util.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import cn.volley.toolbox.Volley;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private MainActivity mView;


    private FeedRecAdapter feedRecAdapter;

    private RecyclerItemClickListener readItemClickListener;

    private boolean refresh = false;

    public MainViewModel(MainActivity view) {
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

        readItemClickListener = new RecyclerItemClickListener(mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int pos = position - 1;
                Feed feed = feedRecAdapter.getItem(pos);
                if (feed == null) return;

                Bundle bundle = new Bundle();
                XLog.d("bundle" + bundle);
                bundle.putLong(Constants.FEED_ID, feed.getId());
                XDispatcher.from(mView).dispatch(new RouterAction(FeedActivity.class, bundle, true));
                XLog.d("pos:" + pos);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Feed feed = feedRecAdapter.getmData().get(position);
                XLog.d("onItemLongClick execute!");
                //短震动
                VibratorHelper.shock(VibratorHelper.TIME.SHORT);
                //TODO 弹出对话框:标记全部已读|刷新|删除|置顶
                Menu menu = ViewUtils.showPopupMenu(mView, view, R.menu.menu_single_feed, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.string.action_read_all:

                                //标记全部已读
                                break;
                            case R.string.action_read_reflash:
                                //刷新

                                break;
                            case R.string.action_read_top:
                                //置顶
                                break;
                            case R.string.action_read_delete_feed:
                                //删除订阅
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }


    /**
     * 更新频道信息
     */
    public void updateFeed(UpdateFeedEvent event) {
        if (feedRecAdapter != null) {
            int p = feedRecAdapter.getmData().indexOf(event.getFeed());
            if (p >= 0) {
                feedRecAdapter.getmData().get(p).setStatus(event.getStatus());
                //更新这个要加上header
                feedRecAdapter.notifyItemChanged(feedRecAdapter.getHeaderSize() + p);
            }
        }
    }

    /**
     * 更新所有频道
     */
    public void updateFeeds() {
        if (feedRecAdapter != null) {
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

    public void onAddButtonClick() {
//        feedRecAdapter.getmData().get(0).save(mView, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                XLog.d("添加数据成");
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
        //DialogFractory.create((Activity) mView, DialogFractory.Type.AddSubscrible).show();
    }

    @Override
    public void clear() {
        mView = null;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }


    /**
     * 更新所有
     */
    public void refreshAll() {
        XLog.d("RefreshAsyncTask refreshAll execute!");
        if (isRefresh()) {
            XLog.d("RefreshAsyncTask 正在刷新!");
            return;
        }
        setRefresh(true);
        RefreshAsyncTask refreshAsyncTask = new RefreshAsyncTask(new RefreshAsyncTask.StatusListener() {
            @Override
            public void onSuccess() {
                setRefresh(false);
            }

            @Override
            public void onCancel() {
                setRefresh(false);
            }
        });
        refreshAsyncTask.execute((ArrayList<Feed>)getFeedRecAdapter().getmData());

    }
}
