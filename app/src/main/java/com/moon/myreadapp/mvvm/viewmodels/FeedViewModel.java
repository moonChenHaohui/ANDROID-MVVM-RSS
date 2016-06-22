package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.ArticleRecAdapter;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshRecyclerView;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.common.components.rss.FeedNetwork;
import com.moon.myreadapp.common.components.toast.TastyToast;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.ModelHelper;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.ui.ArticleWebActivity;
import com.moon.myreadapp.ui.FeedActivity;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.Conver;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.VibratorHelper;
import com.rey.material.app.Dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedViewModel extends BaseViewModel {

    private FeedActivity mView;

    private RecyclerItemClickListener articleClickListener;
    private ArticleRecAdapter mAdapter;

    private long feedId;
    private Feed feed;
    private int currentPosition = -1;
    private Dialog mDialog;
    private boolean showUnReadArticles;


    public FeedViewModel(FeedActivity view, long feedId) {
        this.mView = view;
        this.feedId = feedId;
        this.feed = DBHelper.Query.getFeed(feedId);
        if (feed == null) {
            return;
        }
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {


        this.mView.setTitle(feed.getTitle());
        showUnReadArticles = PreferenceUtils.getInstance(mView).getBooleanParam(Constants.FEED_SHOW_ALL, Constants.showUnReadArticles);
        mAdapter = new ArticleRecAdapter(mView, getBaseData(0, 10));
    }

    private List<Article> getBaseData(int start, int size) {
        return DBHelper.Query.getArticlesByID(feedId, showUnReadArticles ? Article.Status.NORMAL_AND_FAVOR_BUT_UNREAD : Article.Status.NORMAL_AND_FAVOR, start, size);
    }


    @Override
    public void initEvents() {
        articleClickListener = new RecyclerItemClickListener(mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                readArticle(mAdapter.getItem(position), position);
                updateFeed();
                //打开原文还是链接
                boolean isOpenSource = PreferenceUtils.getInstance(mView).getBooleanParam(mView.getString(R.string.set_open_source_key, false));
                if (isOpenSource && mAdapter.getItem(position).getContainer().length() < Constants.MIN_CONTAINER_SIZE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ARTICLE_TITLE, mAdapter.getItem(position).getTitle());
                    bundle.putString(Constants.ARTICLE_URL, mAdapter.getItem(position).getLink());
                    XDispatcher.from(mView).dispatch(new RouterAction(ArticleWebActivity.class, bundle, true));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.ARTICLE_ID, mAdapter.getItem(position).getId());
                    bundle.putInt(Constants.ARTICLE_POS, position);
                    XDispatcher.from(mView).dispatch(new RouterAction(ArticleActivity.class, bundle, true));
                }
            }

            @Override
            public void onItemLongClick(final View view, final int position) {
                //XLog.d("onItemLongClick execute!");
                final Article article = mAdapter.getmData().get(position);
                currentPosition = position;
                //震动
                VibratorHelper.shock(VibratorHelper.TIME.SHORT);

                View v = mView.getLayoutInflater().inflate(R.layout.menu_singer_article, null);

                mDialog = new Dialog(mView) {

                }.
                        contentView(v).
                        cancelable(true).
                        layoutParams(-1, -2);

                mDialog.show();
                //已读
                v.findViewById(R.id.action_read).setVisibility(article.getUse_count() <= 0 ? View.VISIBLE : View.GONE);
                //menu.findItem(R.id.action_read).setVisible(article.getUse_count() <= 0);
                //收藏
                ((Button) (v.findViewById(R.id.action_read_favor))).setText(BuiltConfig.getString(article.getStatus() == Article.Status.FAVOR.status ? R.string.action_favor_back : R.string.action_favor));
                // menu.findItem(R.id.action_read_favor).setTitle(BuiltConfig.getString(article.getStatus() == Article.Status.FAVOR.status ? R.string.action_favor_back : R.string.action_favor));

            }

        });
    }

    public ArticleRecAdapter getmAdapter() {
        return mAdapter;
    }

    public RecyclerItemClickListener getArticleClickListener() {
        return articleClickListener;
    }

    @Override
    public void clear() {
        mView = null;
    }


    /**
     * 刷新
     *
     * @param feedList
     */
    public void refresh(final PullToRefreshRecyclerView feedList) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    Feed newFeed = FeedNetwork.getInstance().load(feed.getUrl());
                    List<Article> articles =feed.getArticles();
                    if (articles == null || articles.size() == 0) {
                        //没有获取到数据
                        return "success";
                    }

                    //result 为获取新更新的文章
                    ArrayList<Article> result = ModelHelper.getUpDateArticlesByFeedId(feedId, articles);


                    boolean haveNewDate = result != null && result.size() > 0;

                    //插入数据库
                    if (haveNewDate) {
                        DBHelper.Insert.articles(result);
                    }
                    articles = null;

                    //设置提示
                    ToastHelper.showNotice(mView, haveNewDate ? BuiltConfig.getString(R.string.notice_update, feed.getTitle(), result.size()) : BuiltConfig.getString(R.string.notice_update_none), TastyToast.STYLE_MESSAGE);

                    //重新设置数据
                    if (haveNewDate) {
                        mAdapter.setmData(getBaseData(0, Constants.SINGLE_LOAD_SIZE));
                    }

                }catch (Exception e){
                    mAdapter.setmData(getBaseData(0, Constants.SINGLE_LOAD_SIZE));
                    return "error";
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //完成刷新
                feedList.onPullDownRefreshComplete();
                if (s.endsWith("success")){
                    //设置刷新时间
                    feedList.getHeaderLoadingLayout().setLastUpdatedLabel(Conver.ConverToString(new Date(), "HH:mm"));
                    updateFeed();
                }

            }
        }.execute();

    }

    /**
     * 加载更多
     */
    public boolean loadMore() {
        List<Article> loadData;
        if (mAdapter.getmData() == null || mAdapter.getmData().size() == 0) {
            loadData = getBaseData(0, Constants.SINGLE_LOAD_SIZE);
        } else {
            loadData = getBaseData(mAdapter.getmData().size(), Constants.SINGLE_LOAD_SIZE);
        }
        if (loadData != null) {
            mAdapter.addAll(loadData);
        }
        if (loadData == null || loadData.size() < Constants.SINGLE_LOAD_SIZE) {
            //没有获得足够的数据,下次加载没有数据了.
            return false;
        } else {
            return true;
        }
    }


    private void readArticle(Article article, int position) {
        if (article == null) return;
        article.setLast_read_time(new Date());
        article.setUse_count(article.getUse_count() + 1);
        DBHelper.UpDate.saveArticle(article);
        mAdapter.notifyItemChanged(position);
    }

    private void updateFeed() {
        UpdateFeedEvent event = new UpdateFeedEvent(feed, UpdateFeedEvent.TYPE.STATUS);
        event.setStatus(UpdateFeedEvent.NORMAL);
        XApplication.getInstance().bus.post(event);
    }

    public void btnOnClick(View v) {

        if (currentPosition >= 0 || currentPosition < mAdapter.getmData().size()) {
            int id = v.getId();
            final Article article = mAdapter.getmData().get(currentPosition);
            switch (id) {
                case R.id.action_read:
                    readArticle(article, currentPosition);
                    getmAdapter().notifyItemChanged(getmAdapter().getWholePosition(currentPosition));
                    break;
                case R.id.action_read_favor:
                    //收藏
                    if (article.getStatus() == Article.Status.NORMAL.status) {
                        article.setStatus(Article.Status.FAVOR.status);
                        DBHelper.UpDate.saveArticle(article);
                        ToastHelper.showNotice(mView, BuiltConfig.getString(R.string.action_favor) + BuiltConfig.getString(R.string.success), TastyToast.STYLE_ALERT).setDuration(1000);
                    } else {
                        article.setStatus(Article.Status.NORMAL.status);
                        DBHelper.UpDate.saveArticle(article);
                        ToastHelper.showNotice(mView, BuiltConfig.getString(R.string.action_favor_back) + BuiltConfig.getString(R.string.success), TastyToast.STYLE_ALERT).setDuration(1000);
                    }
                    //通知更新
                    getmAdapter().notifyItemChanged(getmAdapter().getWholePosition(currentPosition));
                    break;
                case R.id.action_read_delete:
                    //删除
                    if (article.getStatus() == Article.Status.FAVOR.status) {
                        //如果在收藏情况下被删除,且有objid,那么需要通知服务端
                        if (article.getObjectId() != null) {
                            article.delete(mView);
                        }
                    }
                    mAdapter.remove(currentPosition);
                    article.setStatus(Article.Status.DELETE.status);
                    DBHelper.UpDate.saveArticle(article);
                    ToastHelper.showNotice(mView, BuiltConfig.getString(R.string.action_delete) + BuiltConfig.getString(R.string.success), TastyToast.STYLE_ALERT).setDuration(1000);
                    break;
            }

        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void updateSet(boolean showAllArticles) {
        this.showUnReadArticles = showAllArticles;

        mAdapter.setmData(getBaseData(0, Constants.SINGLE_LOAD_SIZE));
        updateFeed();
    }

    public void updateArticleByPosition(int pos, Article article) {
        if (mAdapter.getmData() == null) return;
        if (pos >= 0 && pos < mAdapter.getmData().size()) {
            mAdapter.getmData().set(pos, article);
            mAdapter.notifyItemChanged(mAdapter.getWholePosition(pos));
        }
    }

    @Bindable
    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
        notifyPropertyChanged(BR.feed);
    }
}
