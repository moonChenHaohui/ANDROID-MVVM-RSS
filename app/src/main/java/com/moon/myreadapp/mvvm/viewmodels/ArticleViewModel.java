package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.ui.ArticleWebActivity;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.DBHelper;

import de.halfbit.tinybus.Subscribe;

/**
 * Created by moon on 15/11/14.
 */
public class ArticleViewModel extends BaseViewModel {

    private IView mView;

    private Article article;

    private long articleId;

    public ArticleViewModel(IView view,long id) {
        this.mView = view;
        articleId = id;
        article = DBHelper.Query.getArticle(articleId);

        initViews();
        initEvents();
    }

    @Bindable
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        notifyPropertyChanged(BR.article);
    }

    /**
     * 阅读原文
     */
    public void onClickWebArticle (View view ){
        XDispatcher.from((Activity) mView).dispatch(new RouterAction(ArticleWebActivity.class, true));
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void clear() {

    }
}
