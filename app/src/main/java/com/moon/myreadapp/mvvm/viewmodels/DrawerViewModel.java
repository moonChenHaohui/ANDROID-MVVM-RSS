package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.common.adapter.DrawerAdapter;
import com.moon.myreadapp.mvvm.models.MenuItem;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.mvvm.models.dao.UserDao;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.ui.SettingActivity;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerViewModel extends BaseViewModel {


    private User user;

    private DrawerAdapter drawerAdapter;

    private AdapterView.OnItemClickListener drawerItemClickListener;

    private Activity mView;


    private UserDao userDao;


    public DrawerViewModel(Activity view) {
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
        this.userDao = DBHelper.getDAO().getUserDao();
        if (userDao.queryBuilder().list().size() > 0){
            //有本地用户存在
            setUser(userDao.queryBuilder().list().get(0));
        } else {

        }
        requestUser();
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
        //user = new User(null,"123","123","123");
        if (user == null || StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())){
            //没有用户缓存
            return;
        }
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
    }


    /**
     * 用户点击登陆
     */
    public void onClickLogin (View view ){
        XLog.d("onclick work !");
        if (user == null || StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())){
            XDispatcher.from((Activity)mView).dispatch(new RouterAction(LoginActivity.class, true));
            return;
        } else {
            requestUser();
        }
    }
}
