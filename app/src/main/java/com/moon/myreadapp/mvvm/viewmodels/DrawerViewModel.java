package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.business.RequestHelper;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.DrawerAdapter;
import com.moon.myreadapp.constants.Nav;
import com.moon.myreadapp.mvvm.models.MenuItem;
import com.moon.myreadapp.mvvm.models.User;
import com.moon.myreadapp.ui.MainActivity;
import com.moon.myreadapp.ui.SettingActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;
import com.moon.myreadapp.util.DialogFractory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerViewModel extends BaseViewModel {


    private User user;

    private DrawerAdapter drawerAdapter;

    private AdapterView.OnItemClickListener drawerItemClickListener;

    private IMainView mView;



    public DrawerViewModel(IMainView view) {
        this.mView = view;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        List<MenuItem> menus = new ArrayList<>();
        menus.add(new MenuItem.Builder().title("添加订阅").build());
        menus.add(new MenuItem.Builder().title("推荐频道").build());
        menus.add(new MenuItem.Builder().title("我的收藏").build());
        menus.add(new MenuItem.Builder().title("最近阅读").build());
        menus.add(new MenuItem.Builder().title("离线阅读").build());

        drawerAdapter = new DrawerAdapter(menus);

        drawerItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    DialogFractory.create((Activity)mView, DialogFractory.Type.AddSubscrible).show();
                }
            }
        };
    }

    @Override
    public void initEvents() {
        //requestUser();
    }


    @Override
    public void clear() {
        mView = null;
    }

    @Bindable
    public DrawerAdapter getDrawerAdapter() {
        return drawerAdapter;
    }

    public void setDrawerAdapter(DrawerAdapter drawerAdapter) {
        this.drawerAdapter = drawerAdapter;
        notifyPropertyChanged(BR.drawerAdapter);
    }
    @Bindable
    public AdapterView.OnItemClickListener getDrawerItemClickListener() {
        return drawerItemClickListener;
    }

    public void setDrawerItemClickListener(AdapterView.OnItemClickListener drawerItemClickListener) {
        this.drawerItemClickListener = drawerItemClickListener;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyChange();
    }

    public void onClickFriend(View view) {
        XDispatcher.from((Activity)mView).dispatch(new RouterAction(SettingActivity.class, true));
    }
    public void onLongClick (View view){}




    public void requestUser(){
        HashMap<String, String> params = new HashMap<>();
        params.put("id", "1");
        RequestHelper.call(Nav.USER_LOGIN, Nav.USER_LOGIN, params, new RequestHelper.IResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                setUser(JSON.parseObject(response.toString(),User.class));
                XLog.d(response.toString());
            }

            @Override
            public void onErrorResponse(String error) {
                XLog.d(error);
            }
        }, true);
    }
}
