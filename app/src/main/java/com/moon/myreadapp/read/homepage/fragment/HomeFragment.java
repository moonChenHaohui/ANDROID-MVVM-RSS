package com.moon.myreadapp.read.homepage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moon.appframework.common.business.XBusiness;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XFragment;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.pulltorefresh.ui.PullToRefreshListView;
import com.moon.myreadapp.read.homepage.business.bean.Jocks;
import com.moon.myreadapp.read.homepage.viewmodel.ListViewViewModel;
import com.moon.myreadapp.util.Globals;

import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;

/**
 * Created by moon on 15/10/1.
 */
public class HomeFragment extends XFragment{

    public static String URL = "http://api.1-blog.com/biz/bizserver/xiaohua/list.do";

    @Bind(R.id.main_list)
    PullToRefreshListView listView;

    @Bind(R.id.error_tip)
    TextView errorTip;

    private ListViewViewModel listViewViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page,container,false);
    }

    @Override
    public void initView() {
        errorTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTip.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                //点击刷新
                listView.doPullRefreshing(true,0);
            }
        });
        listViewViewModel = new ListViewViewModel(this,listView);
        loadData();
    }

    public void loadData(){
        XBusiness.load(Globals.getApplication().getApplicationContext(), URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Jocks jocks = JSON.parseObject(response.toString(), Jocks.class);
                initData(jocks);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorView();
            }
        }, Request.Method.GET);
    }

    private void showErrorView() {
        if (listView.getVisibility() == View.VISIBLE){
            listView.setVisibility(View.GONE);
            errorTip.setVisibility(View.VISIBLE);
        }
    }

    private void initData(Jocks jocks){
        listViewViewModel.setListViewData(jocks.getDetail());
        listViewViewModel.bind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listViewViewModel.clear();
    }
}
