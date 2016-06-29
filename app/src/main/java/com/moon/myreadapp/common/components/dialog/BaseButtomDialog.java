package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.moon.myreadapp.R;

/**
 * Created by moon on 15/12/24.
 */
public abstract class BaseButtomDialog extends PopupWindow{

    protected Activity context;
    protected Window mWindow;


    public BaseButtomDialog(Activity context) {
        this.context = context;
        this.mWindow = context.getWindow();


        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.ButtomPopupAnimStyle);

        setContentView();
        init();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                onDimiss();
                WindowManager.LayoutParams lp = mWindow.getAttributes();
                lp.alpha = 1f;
                mWindow.setAttributes(lp);
            }
        });

    }

    public BaseButtomDialog() {
    }

    public void showWithView(View v) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 0.4f;
        mWindow.setAttributes(lp);
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }



    abstract void setContentView();
    abstract void init();
    abstract void onDimiss();

}
