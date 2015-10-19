package com.moon.appframework.common.business;

import android.app.Application;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moon on 15/10/4.
 */
public class XBusiness {

    public static void load (Context context,String url,final Map<String, String> params,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener,final int method){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = params ==null ? null : new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url, jsonObject,
                listener,errorListener);
        mQueue.add(jsonObjectRequest);
    }
}
