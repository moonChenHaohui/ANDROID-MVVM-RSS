package com.moon.myreadapp.common.components.cutebuttombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by moon on 15/12/8.
 */
public class AutoButtomBar extends LinearLayout {

    private static String TAG = AutoButtomBar.class.getSimpleName();

    public AutoButtomBar(Context context) {
        super(context);
        init(context);
    }

    public AutoButtomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoButtomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    LayoutInflater mInflater;

    private void init(Context context){
        mInflater = LayoutInflater.from(context);
        addView(createButton("返回", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XLog.d(TAG + "click execute!");
                ObjectAnimator.ofFloat(AutoButtomBar.this, "translationX", 300).setDuration(1000).start();
            }
        }));
        addView(createButton("返回", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XLog.d(TAG + "click execute!");
                ObjectAnimator.ofFloat(AutoButtomBar.this, "translationX", 300).setDuration(1000).start();
            }
        }));
    }

    private IconTextView createButton(String text,OnClickListener listener){
        IconTextView iconTextView = (IconTextView)mInflater.inflate(R.layout.article_bottom_bar,this);
        iconTextView.setText(text);
        iconTextView.setOnClickListener(listener);
        return (IconTextView)mInflater.inflate(R.layout.article_bottom_bar,null);
    }

}
