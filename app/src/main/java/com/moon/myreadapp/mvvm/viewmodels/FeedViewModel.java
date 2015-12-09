package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XDispatcher;
import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.BuildConfig;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.ArticleRecAdapter;
import com.moon.myreadapp.common.components.recyclerview.RecyclerItemClickListener;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.VibratorHelper;
import com.moon.myreadapp.util.ViewUtils;

import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedViewModel extends BaseViewModel {

    private Activity mView;

    private RecyclerItemClickListener articleClickListener;
    private ArticleRecAdapter mAdapter;

    private long feedId;
    public FeedViewModel(Activity view,long feedId) {
        this.mView = view;
        this.feedId=feedId;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        mAdapter = new ArticleRecAdapter(DBHelper.Query.getArticlesByID(feedId, Article.Status.NORMAL_AND_FAVOR));
    }

    @Override
    public void initEvents() {
        articleClickListener = new RecyclerItemClickListener((Context)mView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putLong(Constants.ARTICLE_ID, mAdapter.getItem(position).getId());
                bundle.putInt(Constants.ARTICLE_POS, position);
                XDispatcher.from((Context)mView).dispatch(new RouterAction(ArticleActivity.class,bundle,true));
            }

            @Override
            public void onItemLongClick(final View view,final int position) {
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
                                if(article.getStatus() == Article.Status.NORMAL.status){
                                    article.setStatus(Article.Status.FAVOR.status);
                                    DBHelper.UpDate.saveArticle(article);
                                    Snackbar.make(view, BuiltConfig.getString(R.string.action_favor) + BuiltConfig.getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(view, BuiltConfig.getString(R.string.action_favor_back), Snackbar.LENGTH_SHORT).show();
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
                menu.findItem(R.id.action_read_favor).setTitle(BuiltConfig.getString(article.getStatus() == Article.Status.FAVOR.status ? R.string.action_favor_back :R.string.action_favor ));

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
        if (event.getPosition() < 0){
            return;
        }
        mAdapter.getmData().get(event.getPosition()).setUse_count(event.getUseCount());
        mAdapter.notifyItemChanged(event.getPosition());
    }
}
