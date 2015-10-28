package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.DrawerAdapter;
import com.moon.myreadapp.mvvm.models.MenuItem;
import com.moon.myreadapp.mvvm.models.User;
import com.moon.myreadapp.ui.MainActivity;
import com.moon.myreadapp.ui.SettingActivity;
import com.moon.myreadapp.ui.base.IViews.IMainView;
import com.moon.myreadapp.util.DialogFractory;

import java.util.ArrayList;
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
    }

    @Override
    public void initViews() {
        user = new User("asdsad","asdasd");
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
    }

    public void onClickFriend(View view) {
        MainActivity.start((Activity)mView, SettingActivity.class);
    }
    public void onLongClick (View view){}
}
