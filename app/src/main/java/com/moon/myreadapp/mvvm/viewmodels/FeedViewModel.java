package com.moon.myreadapp.mvvm.viewmodels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.ArticleRecAdapter;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshRecyclerView;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.components.toast.TastyToast;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.Conver;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.VibratorHelper;
import com.moon.myreadapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedViewModel extends BaseViewModel {

    private Activity mView;

    private RecyclerItemClickListener articleClickListener;
    private ArticleRecAdapter mAdapter;

    private long feedId;
    private Feed feed;

    public FeedViewModel(Activity view, long feedId) {
        this.mView = view;
        this.feedId = feedId;
        this.feed = DBHelper.Query.getFeed(feedId);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        mAdapter = new ArticleRecAdapter(getBaseData());
    }

    private List<Article> getBaseData() {
        return DBHelper.Query.getArticlesByID(feedId, Article.Status.NORMAL_AND_FAVOR);
    }


    @Override
    public void initEvents() {
        articleClickListener = new RecyclerItemClickListener(mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putLong(Constants.ARTICLE_ID, mAdapter.getItem(position).getId());
                bundle.putInt(Constants.ARTICLE_POS, position);
                XDispatcher.from(mView).dispatch(new RouterAction(ArticleActivity.class, bundle, true));
            }

            @Override
            public void onItemLongClick(final View view, final int position) {
                //XLog.d("onItemLongClick execute!");
                final Article article = mAdapter.getmData().get(position);
                //震动
                VibratorHelper.shock(VibratorHelper.TIME.SHORT);
                // 弹出对话框:收藏|已读|删除
                Menu menu = ViewUtils.showPopupMenu(mView, view.findViewById(R.id.article_title), R.menu.menu_single_article, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_read:
                                //标记已读
                                article.setUse_count(article.getUse_count() + 1);
                                DBHelper.UpDate.saveArticle(article);

                                mAdapter.notifyItemChanged(position);
                                break;
                            case R.id.action_read_favor:
                                //收藏
                                if (article.getStatus() == Article.Status.NORMAL.status) {
                                    article.setStatus(Article.Status.FAVOR.status);
                                    DBHelper.UpDate.saveArticle(article);
                                    Snackbar.make(view, BuiltConfig.getString(R.string.action_favor) + BuiltConfig.getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                } else {
                                    article.setStatus(Article.Status.NORMAL.status);
                                    DBHelper.UpDate.saveArticle(article);
                                    Snackbar.make(view, BuiltConfig.getString(R.string.action_favor_back) + BuiltConfig.getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.action_read_delete:
                                //删除
                                mAdapter.remove(position);
                                article.setStatus(Article.Status.DELETE.status);
                                DBHelper.UpDate.saveArticle(article);

                                Snackbar.make(view, BuiltConfig.getString(R.string.action_delete) + BuiltConfig.getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });

                //操作menu行为

                //已读
                menu.findItem(R.id.action_read).setVisible(article.getUse_count() <= 0);
                //收藏
                menu.findItem(R.id.action_read_favor).setTitle(BuiltConfig.getString(article.getStatus() == Article.Status.FAVOR.status ? R.string.action_favor_back : R.string.action_favor));

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


    public void updateArticleUseCount(UpdateArticleEvent event) {
        //XLog.d("updateArticleUseCount" + event.getPosition() + ",count:" + event.getUseCount());
        if (event.getPosition() < 0) {
            return;
        }
        mAdapter.getmData().get(event.getPosition()).setUse_count(event.getUseCount());
        mAdapter.notifyItemChanged(event.getPosition());
    }

    /**
     * 刷新
     *
     * @param feedList
     */
    public void refresh(final PullToRefreshRecyclerView feedList) {
        RssHelper.getMostRecentNews(feed.getUrl(), new RssHelper.IRssListener() {
            @Override
            public void onSuccess(final SyndFeed syndFeed) {
                feedList.post(new Runnable() {
                    @Override
                    public void run() {

                        //设置刷新时间
                        feedList.getHeaderLoadingLayout().setLastUpdatedLabel(Conver.ConverToString(new Date(), "HH:mm"));
                        //完成刷新
                        feedList.onPullDownRefreshComplete();


                        //获取的文章list
                        Feed feed = DBHelper.Util.feedConert(syndFeed, DBHelper.Query.getUserId());
                        ArrayList<Article> articles = DBHelper.Util.getArticles(syndFeed);
                        if (articles == null || articles.size() == 0) {
                            //没有获取到数据
                            return;
                        }

                        //从数据库中取最新的文章,用时间比对
                        Article recentArticle = DBHelper.Query.getRecentArticleOnFeedByFeedId(feedId);
                        Date date = recentArticle.getPublishtime();
                        Date temp;
                        ArrayList<Article> result = new ArrayList<Article>();
                        for (int i = 0; i < articles.size(); i++) {
                            articles.get(i).setFeed_id(feedId);
                            temp = articles.get(i).getPublishtime();
                            if (temp != null && temp.after(date)) {
                                result.add(articles.get(i));
                            }
                        }
                        //result 为获取新更新的文章

                        boolean haveNewDate = result != null && result.size() > 0;
                        //插入数据库
                        if (haveNewDate) {
                            DBHelper.Insert.articles(result);
                        }
                        articles = null;

                        //设置提示
                        showNotice(feedList, haveNewDate ? BuiltConfig.getString(R.string.notice_update,feed.getTitle(), result.size()) : BuiltConfig.getString(R.string.notice_update_none));

                        //重新设置数据
                        if (haveNewDate) {
                            mAdapter.setmData(getBaseData());
                        }

                    }
                });

            }

            @Override
            public void onError(final String msg) {
                showNotice(feedList, BuiltConfig.getString(R.string.notice_update_none));
                feedList.onPullDownRefreshComplete();
//
            }
        });
    }

    /**
     * 弹出顶部提示
     *
     * @param feedList
     * @param txt
     */
    private void showNotice(final PullToRefreshRecyclerView feedList, String txt) {
        TastyToast.makeText(mView, txt, TastyToast.STYLE_MESSAGE).enableSwipeDismiss().setLayoutBelow(mView.findViewById(R.id.toolbar)).show();
        /*
        final View view = LayoutInflater.from(mView).inflate(R.layout.common_notice_bar, null);

        feedList.getmAdapter().addHeader(view);

        View m =feedList.getmAdapter().getHeader(0);
        final TextView tv = (TextView) m.findViewById(R.id.info);
        tv.setText(txt + time);
        //设置放大的动画
        if (time % 2 == 0) {
            animate(tv).setDuration(1000).scaleXBy(.5f).scaleYBy(.5f).setInterpolator(new AnticipateOvershootInterpolator());
        } else {
            animate(tv).setDuration(1000).scaleXBy(-.5f).scaleYBy(-.5f).setInterpolator(new AnticipateOvershootInterpolator());
        }
        time++;
        feedList.postDelayed(new Runnable() {
            @Override
            public void run() {
                feedList.getmAdapter().removeHeader(view);

            }
        }, 2000);
        */
    }

}
