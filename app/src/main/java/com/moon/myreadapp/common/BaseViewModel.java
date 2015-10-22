package com.moon.myreadapp.common;

import android.databinding.BaseObservable;

public abstract class BaseViewModel extends BaseObservable{



    public abstract void initViews();

    public abstract void initEvents();
    public abstract void clear();

}
