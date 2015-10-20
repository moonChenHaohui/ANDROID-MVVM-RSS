package com.moon.myreadapp.mvvm.viewmodels;

import com.moon.myreadapp.common.BaseViewModel;
import com.moon.myreadapp.ui.IMainView;

/**
 * Created by moon on 15/10/19.
 */
public class MainViewModel extends BaseViewModel {

    private IMainView mView;

    public MainViewModel(IMainView view){
        this.mView = view;
    }


    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void bind() {

    }

    @Override
    public void clear() {
        mView = null;
    }
}
