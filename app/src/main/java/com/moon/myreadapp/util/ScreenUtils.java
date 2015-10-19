package com.moon.myreadapp.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.reflect.Field;

/**
 * 获取屏幕信息 分辨率\尺寸
 */
public class ScreenUtils {

    public static DisplayMetrics dm = null;
    private static Resources resources = null;

    /**
     * 获取 DisplayMetrics
     * @return
     */
    private static DisplayMetrics getDisplayMetrics() {
        if (null == dm) {
            dm = getResources().getDisplayMetrics();
        }
        return dm;
    }

    public static Resources getResources() {
        if (null == resources) {
            resources = Globals.getApplication().getResources();
        }
        return resources;
    }

    private ScreenUtils() {
    }


    public static int dpToPx(int dp) {
        int px = Math.round(dp * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
