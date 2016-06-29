package com.moon.appframework.common.business;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        final String mRequestBody = appendParameter(URL,params);
        JsonObjectRequest req = new JsonObjectRequest(isPost ? Request.Method.POST : Request.Method.GET,URL, params == null ? new JSONObject() : new JSONObject(params),
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


        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
            }

            @Override
            public byte[] getBody() {

                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, PROTOCOL_CHARSET);
                    return null;
                }
            }
        };

        if (StringUtils.isNotEmpty(tag)){
            req.setTag(tag);
            XApplication.getInstance().addToRequestQueue(req,tag);
        } else {
            XApplication.getInstance().addToRequestQueue(req);
        }

    }

    private static String appendParameter(String url,Map<String,String> params){
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for(Map.Entry<String,String> entry:params.entrySet()){
            builder.appendQueryParameter(entry.getKey(),entry.getValue());
        }
        return builder.build().getQuery();
    }

    public static void cancel(final String tag){
        XApplication.getInstance().cancelPendingRequests(tag);
    }

    public interface IResponseListener {

        /**
         * 请求正确返回的数据
         * @param response
         */
        void onResponse(JSONObject response);


        /**
         * error返回的字符串,已经在VolleyErrorHelper作出处理
         * @param error
         */
        void onErrorResponse(String error);
    }
}
