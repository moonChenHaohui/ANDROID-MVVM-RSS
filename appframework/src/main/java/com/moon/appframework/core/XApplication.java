package com.moon.appframework.core;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.moon.appframework.util.LruImageCache;

import de.halfbit.tinybus.TinyBus;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class XApplication extends Application{

    private static XApplication application;
    public TinyBus bus;

    public static final String TAG = "VolleyPatterns";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static synchronized XApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        application = this;
        // Event Bus init
        bus = TinyBus.from(this);


    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            LruImageCache lruImageCache = LruImageCache.instance();
            mImageLoader = new ImageLoader(getRequestQueue(),lruImageCache);
        }
        return mImageLoader;
    }
    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }



    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
