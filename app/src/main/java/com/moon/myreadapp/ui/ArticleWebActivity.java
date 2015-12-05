package com.moon.myreadapp.ui;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.scroll.ObservableWebView;
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
                if (url != null && url.startsWith("http://"))
                    XLog.d("download success!");
            }
        });

        return webView;
    }


    @Override
    protected void onStart() {
        super.onStart();
        title = getIntent().getExtras().getString(Constants.ARTICLE_TITLE,getResources().getString(R.string.title_activity_article_web));
        url = getIntent().getExtras().getString(Constants.ARTICLE_URL,null);
        if (url != null) {
            webView.loadUrl(url);
        }
        getSupportActionBar().setTitle(title);

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
