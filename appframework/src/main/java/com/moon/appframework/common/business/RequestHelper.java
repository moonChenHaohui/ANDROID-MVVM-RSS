package com.moon.appframework.common.business;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by moon on 15/11/7.
 * <p/>
 * 网络请求帮助类
 */
public class RequestHelper {

    public static void call(final String URL,final String tag, HashMap<String, String> params,final IResponseListener listener, boolean isPost) {
        if (listener == null) {
            return;
        }
        JsonObjectRequest req = new JsonObjectRequest(isPost ? Request.Method.POST : Request.Method.GET,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(VolleyErrorHelper.getMessage(error, XApplication.getInstance()));
            }
        });
        if (StringUtils.isNotEmpty(tag)){
            req.setTag(tag);
            XApplication.getInstance().addToRequestQueue(req,tag);
        } else {
            XApplication.getInstance().addToRequestQueue(req);
        }
    }

    interface IResponseListener {

        /**
         * 请求正确返回的数据
         * @param response
         */
        public void onResponse(JSONObject response);


        /**
         * error返回的字符串,已经在VolleyErrorHelper作出处理
         * @param error
         */
        public void onErrorResponse(String error);
    }
}
