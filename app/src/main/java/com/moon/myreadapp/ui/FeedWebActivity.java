package com.moon.myreadapp.ui;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.scroll.ObservableWebView;
import com.moon.myreadapp.ui.base.ToolBarExpandActivity;

public class FeedWebActivity extends ToolBarExpandActivity<ObservableWebView> {
    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_feed_web;
    }

    //private ActivityFeedWebBinding binding;
    private Toolbar toolbar;


    @Override
    protected ObservableWebView createScrollable() {
        ObservableWebView webView = (ObservableWebView) findViewById(R.id.scrollable);
        //webView.loadUrl("file:///android_asset/lipsum.html");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    XLog.d("download success!");
            }
        });

        webView.loadUrl("http://blog.sina.com.cn/s/blog_7a66361301011a46.html");
        return webView;
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
