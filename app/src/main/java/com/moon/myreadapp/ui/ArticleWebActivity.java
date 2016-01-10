package com.moon.myreadapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.scroll.ObservableWebView;
import com.moon.myreadapp.common.components.webview.ILoad;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.ui.base.ToolBarExpandActivity;

public class ArticleWebActivity extends ToolBarExpandActivity<ObservableWebView> {
    @Override
    protected Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_article_web;
    }

    private ObservableWebView webView;

    private String title;
    private String url;

    @Override
    protected ObservableWebView createScrollable() {
        webView = (ObservableWebView) findViewById(R.id.scrollable);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))){
                    mToolbar.setTitle(getResources().getString(R.string.xsearch_loading));
                }
            }

        });
        return webView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        webView.setListener(new ILoad() {

            @Override
            public void start(WebView view, String url) {
                ArticleWebActivity.this.url = url;
                mToolbar.setTitle(getString(R.string.xsearch_loading));
            }

            @Override
            public void success(WebView view, String u) {
                mToolbar.setTitle(view.getTitle());
            }

            @Override
            public void error(WebView view, int errorCode, String description, String failingUrl) {
                mToolbar.setTitle(getString(R.string.article_web_view_error_url));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        title = getIntent().getExtras().getString(Constants.ARTICLE_TITLE,getResources().getString(R.string.title_activity_article_web));
        url = getIntent().getExtras().getString(Constants.ARTICLE_URL,null);
        load(url);
        mToolbar.setTitle(title);

    }

    private void load (String url){
        if (url != null) {
            webView.loadUrl(url);
            mToolbar.setTitle(getResources().getString(R.string.xsearch_loading));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.content) {
            finish();
        } else if (id == R.id.action_refresh) {
            webView.loadUrl(url);
        }

        return super.onOptionsItemSelected(item);
    }
}
