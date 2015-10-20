package com.moon.myreadapp.common;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.moon.myreadapp.util.ScreenUtils;

public abstract class BaseViewModel {



    public abstract void initViews();

    public abstract void initEvents();

    public abstract void bind();

    public abstract void clear();

    public void setText(TextView view, String text){
        if(TextUtils.isEmpty(text)){
            view.setVisibility(View.GONE);
            return;
        }

        view.setText(text);
    }


    public int dpToPx(int dp) {
        return ScreenUtils.dpToPx(dp);
    }
}
