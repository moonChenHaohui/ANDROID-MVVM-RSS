package com.moon.myreadapp.ui.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by moon on 15/12/22.
 */
public class ComProgressBar extends ViewGroup {

    public View.OnClickListener listener;
    public TextView messageView;
    public ProgressBar progressBar;

    public ComProgressBar(Context context) {
        super(context);
    }

    public ComProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMessage(String message){

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
