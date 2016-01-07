package com.moon.myreadapp.common.components.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.moon.myreadapp.common.components.scroll.ObservableScrollViewCallbacks;
import com.moon.myreadapp.common.components.scroll.Scrollable;

/**
 * Created by moon on 15/10/24.
 */
public class ProgressWebView extends WebView implements Scrollable{
    @Override
    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {

    }

    @Override
    public void addScrollViewCallbacks(ObservableScrollViewCallbacks listener) {

    }

    @Override
    public void removeScrollViewCallbacks(ObservableScrollViewCallbacks listener) {

    }

    @Override
    public void clearScrollViewCallbacks() {

    }

    @Override
    public void scrollVerticallyTo(int y) {

    }

    @Override
    public int getCurrentScrollY() {
        return 0;
    }

    @Override
    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {

    }

    private ProgressBar progressbar;
    private ILoad listener;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 10, 0, 0));

        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (listener != null) {
                    listener.success(view,url);
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (listener != null) {
                    listener.error(view, errorCode, description, failingUrl);
                }
            }
        });
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public ILoad getListener() {
        return listener;
    }

    public void setListener(ILoad listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}