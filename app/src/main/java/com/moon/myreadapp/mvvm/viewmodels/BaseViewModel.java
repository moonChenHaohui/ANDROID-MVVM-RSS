package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.BaseObservable;

import com.moon.myreadapp.ui.base.IViews.IMainView;

public abstract class BaseViewModel extends BaseObservable{


    public abstract void initViews();

    public abstract void initEvents();
    public abstract void clear();

}
