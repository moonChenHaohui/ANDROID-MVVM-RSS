package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.htmlTextView.HtmlRemoteImageGetter;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityArticleBinding;
import com.moon.myreadapp.mvvm.viewmodels.ArticleViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

public class ArticleActivity extends BaseActivity {


    private Toolbar toolbar;
    private ActivityArticleBinding binding;

    private ArticleViewModel articleViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);
    }

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_article;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());

        articleViewModel = new ArticleViewModel(this, getIntent().getExtras().getLong(Constants.ARTICLE_ID, -1));
        binding.setArticleViewModel(articleViewModel);
        Spanned spanned = Html.fromHtml(articleViewModel.getArticle().getContainer(), new HtmlRemoteImageGetter(binding.feedBody.feedContent,null), null);
//        XLog.d("spanned:" + spanned.toString());
        binding.feedBody.feedContent.setText(spanned);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.content) {
            finish();
        } else if (id == R.id.action_read_all) {
            XDispatcher.from(this).dispatch(new RouterAction(ArticleWebActivity.class, true));
        }
        return super.onOptionsItemSelected(item);
    }
}
