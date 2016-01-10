package com.moon.myreadapp.common.components.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by moon on 15/10/24.
 */
public interface ILoad {
    void start(WebView view, String url);
    void success(WebView view, String url);
    void error(WebView view, int errorCode,
               String description, String failingUrl);
}
