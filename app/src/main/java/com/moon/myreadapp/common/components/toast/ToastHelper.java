package com.moon.myreadapp.common.components.toast;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.moon.myreadapp.R;
import com.moon.myreadapp.application.ReadApplication;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.ScreenUtils;

/**
 * Created by moon on 15/12/24.
 */
public class ToastHelper {

    public static void showToast(String str) {
        Toast toast = Toast.makeText(ReadApplication.getInstance(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setMargin(0,0.1f);
        toast.show();
    }

    public static void showToast(@StringRes int strid) {
        showToast(Globals.getApplication().getString(strid));
    }

    public static TastyToast showNotice(Activity view, String txt, TastyToast.Style style) {
        TastyToast toast = TastyToast.makeText(view, txt, style).enableSwipeDismiss().setLayoutBelow(view.findViewById(R.id.toolbar));
        toast.setOutAnimation(AnimationUtils.loadAnimation(view, R.anim.toast_out));
        toast.show();
        return toast;
    }
}
