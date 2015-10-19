package com.moon.myreadapp.read.homepage.viewmodel;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moon.appframework.common.business.XBusiness;
import com.moon.myreadapp.common.BaseViewModel;
import com.moon.myreadapp.common.pulltorefresh.ui.PullToRefreshBase;
import com.moon.myreadapp.common.pulltorefresh.ui.PullToRefreshListView;
import com.moon.myreadapp.read.homepage.business.bean.Jock;
import com.moon.myreadapp.read.homepage.business.bean.Jocks;
import com.moon.myreadapp.read.homepage.fragment.HomeFragment;
import com.moon.myreadapp.read.homepage.viewhelper.ListViewAdapter;
import com.moon.myreadapp.util.Globals;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moon on 15/10/4.
 */
public class ListViewViewModel extends BaseViewModel {

    private PullToRefreshListView listView;
    private ListViewAdapter adapter;
    private HomeFragment mFragment;

    private int mPage = 0;
    private static int SIZE = 20;
    public ListViewViewModel(HomeFragment fragment, PullToRefreshListView listView) {
        this.mFragment = fragment;
        this.listView = listView;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(true);
        adapter = new ListViewAdapter();
        //listView.getRefreshableView().addHeaderView(LayoutInflater.from(listView.getContext()).inflate(R.layout.pull_to_refresh_header,null));
        listView.getRefreshableView().setAdapter(adapter);
    }

    @Override
    public void initEvents() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                mFragment.loadData();
                mPage = 0;
                listView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                //listView.setHasMoreData(false);
                mPage++;
                Map<String,String> params = new HashMap<String, String>();
                params.put("page", mPage + "");
                params.put("size", SIZE + "");
                loadMoreData(params);
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onPullUpRefreshComplete();
                    }
                }, 2000);
            }
        });
        //delay刷新
        //listView.doPullRefreshing(true,5000);
    }

    public void setListViewData(List<Jock> jocks) {
        adapter.setModel(jocks);
    }

    @Override
    public void bind() {

    }

    private void loadMoreData(Map<String,String> parms){
        XBusiness.load(Globals.getApplication().getApplicationContext(), HomeFragment.URL + "?page="+parms.get("page") + "&size="+parms.get("size"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Jocks jocks = JSON.parseObject(response.toString(), Jocks.class);
                adapter.addModel(jocks.getDetail());
                bind();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }, Request.Method.GET);
    }


    @Override
    public void clear() {
        this.mFragment = null;
    }
}
