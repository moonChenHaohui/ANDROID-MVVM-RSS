package com.moon.myreadapp.common.components.toast;

import android.view.Gravity;
import android.widget.Toast;

import com.moon.myreadapp.R;
import com.moon.myreadapp.application.ReadApplication;
import com.moon.myreadapp.util.BuiltConfig;

/**
 * Created by moon on 15/12/24.
 */
public class SimpleToastHelper {

    public static void showToast(String str) {
        Toast toast = Toast.makeText(ReadApplication.getInstance(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void showToastNetworkError() {
        Toast toast = Toast.makeText(ReadApplication.getInstance(), BuiltConfig.getString(R.string.request_error), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
