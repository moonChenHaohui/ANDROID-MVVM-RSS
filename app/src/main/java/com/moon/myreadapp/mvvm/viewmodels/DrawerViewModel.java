package com.moon.myreadapp.mvvm.viewmodels;

import com.moon.myreadapp.common.BaseViewModel;
import com.moon.myreadapp.databinding.ActivityMainBinding;
import com.moon.myreadapp.databinding.LeftDrawerContentBinding;
import com.moon.myreadapp.mvvm.models.BaseMenuItem;
import com.moon.myreadapp.mvvm.models.User;
import com.moon.myreadapp.ui.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerViewModel extends BaseViewModel {


    private IMainView mView;
    private LeftDrawerContentBinding binding;

    public DrawerViewModel(IMainView view, LeftDrawerContentBinding binding) {
        this.mView = view;
        this.binding = binding;
        initViews();
        bind();
    }

    @Override
    public void initViews() {
        List<BaseMenuItem> menus = new ArrayList<>();
        menus.add(new BaseMenuItem.Builder().title("添加订阅").build());
        menus.add(new BaseMenuItem.Builder().title("推荐频道").build());
        menus.add(new BaseMenuItem.Builder().title("我的收藏").build());
        menus.add(new BaseMenuItem.Builder().title("最近阅读").build());
        menus.add(new BaseMenuItem.Builder().title("离线阅读").build());
        mView.initDrawer(menus);
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void bind() {
        binding.setUser(new User("moon", "237253995@qq.com"));
    }

    @Override
    public void clear() {
        mView = null;
    }
}
