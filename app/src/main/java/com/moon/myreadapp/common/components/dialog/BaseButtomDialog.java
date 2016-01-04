package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.tool.Binding;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.facebook.drawee.view.DraweeView;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.SimpleToastHelper;
import com.moon.myreadapp.databinding.FragmentShareBinding;
import com.moon.myreadapp.mvvm.models.ShareItem;

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
        //TODO some thing
        lp.alpha = 0.4f;
        mWindow.setAttributes(lp);
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }



    abstract void setContentView();
    abstract void init();
    abstract void onDimiss();

}
