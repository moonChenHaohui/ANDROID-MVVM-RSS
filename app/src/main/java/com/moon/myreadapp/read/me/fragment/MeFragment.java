package com.moon.myreadapp.read.me.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moon.appframework.core.XFragment;

/**
 * Created by moon on 15/10/1.
 */
public class MeFragment extends XFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button btn = new Button(getContext());
        btn.setText("hello wolrd! This is Me");
        return btn;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

    }
}
