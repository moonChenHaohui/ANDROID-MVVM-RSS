package com.moon.appframework.core;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class XActivity extends AppCompatActivity {

    private static XActivity activity;

    public static XActivity getInstance(){
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;
    }

    @SuppressWarnings("unchecked")
     public <T extends View> T getView(int id) {
        T result = (T)findViewById(id);
        if (result == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(id)
                    + " doesn't exist");
        }
        return result;
    }

    protected void setFragmentLayoutId(@IdRes int resId){
        XFragmentManager.setLayoutId(resId);
    }

    protected void injectView(Activity target){
        ButterKnife.bind(target);
    }

}
