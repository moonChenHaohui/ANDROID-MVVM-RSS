package com.moon.myreadapp.common.components.pulltorefresh;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by moon on 15/11/7.
 * 这个类封装了一个基本的empty view
 */
public class EmptyLayout extends FrameLayout {

    private TextView emptyView;

    public EmptyLayout(Context context) {
        super(context);
        emptyView = new TextView(context);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("网络开小差了,请点击刷新");
        emptyView.setGravity(Gravity.CENTER);
        addView(emptyView);
    }
}
