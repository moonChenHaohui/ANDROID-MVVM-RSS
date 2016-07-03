package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.AddSubViewPagerAdapter;
import com.moon.myreadapp.common.adapter.SystemRecAdapter;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.RequestFeed;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.AddFeedActivity;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by moon on 15/12/17.
 */
public class AddFeedViewModel extends BaseViewModel {

    private SystemRecAdapter systemRecAdapter;


    private AddFeedActivity mView;

    private SearchView.OnQueryTextListener searchListener;

    public AddFeedViewModel(AddFeedActivity mView) {
        this.mView = mView;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        systemRecAdapter = new SystemRecAdapter(mView, null);
    }

    @Override
    public void initEvents() {
        searchListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mView.getBinding().searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    search(newText);
                }
                return true;
            }
        };

    }

    private void search(final String info) {
        XApplication.getInstance().cancelPendingRequests(this);
        final String requestUrl = Constants.RSS_REQUEST_URL + StringHelper.getStringUTF8(info);
        mView.getBinding().progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!info.equals(mView.getBinding().searchView.getQuery().toString())) {
                            return;
                        }
                        //XLog.d(response.toString());
                        List<RequestFeed> requestFeeds = null;
                        try {
                            List<String> titleList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String json = jsonObject.getString("results");
                            requestFeeds = new Gson().fromJson(json, new TypeToken<List<RequestFeed>>() {
                            }.getType());
                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }
                        if (null == requestFeeds || requestFeeds.size() == 0) {
                            //
                        }

                        List<Feed> searchFeeds = new ArrayList<Feed>();
                        for (RequestFeed feed : requestFeeds) {
                            searchFeeds.add(DBHelper.Util.feedConert(feed, DBHelper.Query.getUserId()));
                        }
                        systemRecAdapter.setmData(searchFeeds);
                        mView.getBinding().progress.setVisibility(View.INVISIBLE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        XLog.d(error.toString());
                        mView.getBinding().progress.setVisibility(View.INVISIBLE);
                    }
                }
        );
        request.setTag(this);
        Volley.newRequestQueue(mView).add(request);
    }

    @Override
    public void clear() {
        mView = null;
    }



    @Bindable
    public SystemRecAdapter getSystemRecAdapter() {
        return systemRecAdapter;
    }

    /**
     * 点击自定义输入 url 的dialog
     */
    public void onClickAddSub(View view) {
        DialogFractory.createDialog(mView, DialogFractory.Type.AddSubscrible).show();
    }

    @Bindable
    public SearchView.OnQueryTextListener getSearchListener() {
        return searchListener;
    }

    public void setSystemRecAdapter(SystemRecAdapter systemRecAdapter) {
        this.systemRecAdapter = systemRecAdapter;
        notifyPropertyChanged(BR.systemRecAdapter);
    }

    /**
     * 加载服务端数据
     *
     * @param emptyView
     */
    public void loadSystemData(final View emptyView) {
        if (emptyView == null) {
            return;
        }
        emptyView.setEnabled(false);
        emptyView.setVisibility(View.VISIBLE);
        BmobQuery<Feed> query = new BmobQuery<Feed>();
        query.findObjects(mView, new FindListener<Feed>() {
            @Override
            public void onSuccess(List<Feed> object) {
                for (Feed feed : object) {
                    feed.setObjectId(null);
                    feed.clearBmobData();
                }
                systemRecAdapter.setmData(object);
                if (object.size() <= 0) {

                }
                emptyView.setVisibility(object.size() <= 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onError(int code, String msg) {
                emptyView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
