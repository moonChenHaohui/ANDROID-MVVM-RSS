package com.moon.myreadapp.common.components.webview;

import android.webkit.WebView;

/**
 * Created by moon on 15/10/24.
 */
public interface ILoad {

    void success(WebView view, String url);
    void error(WebView view, int errorCode,
               String description, String failingUrl);
}
