package com.moon.myreadapp.common.components.toast;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moon.myreadapp.R;
import com.moon.myreadapp.application.ReadApplication;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.ScreenUtils;

/**
 * Created by moon on 15/12/24.
 */
public class SimpleToastHelper {

    public static void showToast(String str) {
        Toast toast = Toast.makeText(ReadApplication.getInstance(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setMargin(0,0.01f);
        toast.show();
    }

    public static void showToastNetworkError() {
        showToast(BuiltConfig.getString(R.string.request_error));
    }
}
