package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.FeedRecAdapter;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.ui.MainActivity;
import com.moon.myreadapp.ui.helper.AsyncTaskRefresh;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.VibratorHelper;
import com.rey.material.app.Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private MainActivity mView;


    private FeedRecAdapter feedRecAdapter;

    private RecyclerItemClickListener readItemClickListener;


    private boolean refresh = false;
    private int currentPosition = -1;
    private Dialog mDialog;

    private AsyncTaskRefresh refreshAsyncTask;

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
        feedRecAdapter = new FeedRecAdapter(mView,feeds);

        readItemClickListener = new RecyclerItemClickListener(mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //应当减去 head 的size 作为正确的pos
                int pos = position -feedRecAdapter.getHeaderSize();
                if (feedRecAdapter.getmData() == null) return;
                if (pos < 0 || pos >= feedRecAdapter.getmData().size()) return;
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
                //应当减去 head 的size 作为正确的pos
                int pos = position -feedRecAdapter.getHeaderSize();
                //非空判断
                if (feedRecAdapter.getmData() == null) return;
                if (pos < 0 || pos >= feedRecAdapter.getmData().size()) return;

                Feed feed = feedRecAdapter.getmData().get(pos);
                //XLog.d("onItemLongClick execute!");
                //短震动
                VibratorHelper.shock(VibratorHelper.TIME.SHORT);
                currentPosition = pos;
                //弹出对话框:标记全部已读|刷新|删除|置顶
                mDialog = new Dialog(mView).
                        contentView(mView.getLayoutInflater().inflate(R.layout.menu_singer_feed,null)).
                        cancelable(true).
                        layoutParams(-1, -2);
                mDialog.show();
//                Menu menu = ViewUtils.showPopupMenu(mView, view, R.menu.menu_single_feed, new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(android.view.MenuItem item) {
//                        int id = item.getItemId();
//                        switch (id) {
//                            case R.string.action_read_all:
//
//                                //标记全部已读
//                                break;
//                            case R.string.action_read_reflash:
//                                //刷新
//
//                                break;
//                            case R.string.action_read_top:
//                                //置顶
//                                break;
//                            case R.string.action_read_delete_feed:
//                                //删除订阅
//                                break;
//                        }
//                        return false;
//                    }
//                });
            }
        });
    }


    /**
     * 更新频道信息
     */
    public void updateFeed(UpdateFeedEvent event) {
        if (feedRecAdapter != null && feedRecAdapter.getmData() != null) {
            int p = feedRecAdapter.getmData().indexOf(event.getFeed());
            if (p >= 0) {
                feedRecAdapter.getmData().get(p).setStatus(event.getStatus());
                //ToastHelper.showNotice(mView,event.getNotice(), TastyToast.STYLE_ALERT);
                XLog.d("RefreshAsyncTaskfeed:status:" + event.getStatus());
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



    @Override
    public void clear() {
        if (refreshAsyncTask != null && refreshAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            refreshAsyncTask.cancel(true);
            refreshAsyncTask = null;
        }
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
        refresh((ArrayList<Feed>)getFeedRecAdapter().getmData());
    }

    /**
     * 更新单个
     * @param feed
     */
    public void refreshSingle(Feed feed){
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        feeds.add(feed);
        refresh(feeds);
    }
    public void refresh(ArrayList<Feed> feeds){
        if (isRefresh()) {
            return;
        }
        setRefresh(true);
        if (refreshAsyncTask != null && refreshAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            refreshAsyncTask.cancel(true);

            refreshAsyncTask = null;
        }
        refreshAsyncTask = new AsyncTaskRefresh(new AsyncTaskRefresh.StatusListener() {
            @Override
            public void onSuccess() {
                setRefresh(false);
            }

            @Override
            public void onCancel() {
                setRefresh(false);
            }
        });
        refreshAsyncTask.execute(feeds);
    }

    /**
     * 弹出菜单操作
     * @param v
     */
    public void btnOnClick(View v) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismissImmediately();
        }
        if (currentPosition >= 0 || currentPosition < feedRecAdapter.getmData().size()){
            final Feed feed = feedRecAdapter.getmData().get(currentPosition);
            int id = v.getId();
            switch (id) {
                case R.id.action_read_all:
                    DBHelper.UpDate.readAllArticleFromFeed(feed);
                    feedRecAdapter.notifyItemChanged(currentPosition);
                    ToastHelper.showToast(R.string.notice_read_all);
                    //标记全部已读
                    break;
                case R.id.action_read_reflash:
                    //刷新
                    refreshSingle(feed);
                    break;
//                case R.id.action_read_top:
//                    //置顶
//                    ToastHelper.showToast("top top top");
//                    break;
                case R.id.action_read_delete_feed:
                    //删除
                    final Dialog dialog = new Dialog(mView){
                    }.title(mView.getString(R.string.feed_item_delete_title,feed.getTitle()))
                            .positiveAction(R.string.feed_item_delete_commit)
                            .negativeAction(R.string.feed_item_delete_cancel).cancelable(true);
                    dialog.negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }).positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            feedRecAdapter.remove(currentPosition);
                            //删除订阅
                            DBHelper.Delete.deleteFeed(feed);
                            //通知服务端删除
                            if(feed.getObjectId() != null){
                                feed.delete(mView);
                            }
                            ToastHelper.showToast(R.string.feed_item_delete_success);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
            }
        }

    }

}
