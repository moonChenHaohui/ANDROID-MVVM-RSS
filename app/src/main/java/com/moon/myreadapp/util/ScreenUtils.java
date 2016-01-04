package com.moon.myreadapp.util;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 获取屏幕信息 分辨率\尺寸
 */
public class ScreenUtils {

    private static DisplayMetrics dm = null;
    private static Resources resources = null;

    /**
     * 获取 DisplayMetrics
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
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
        int px = Math.round(dp * (getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    /**
     * 获取屏幕亮度
     * @param activity
     * @return
     */
    public static int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {

        }
        return value;
    }

    /**
     * 保存亮度
     * @param resolver
     * @param brightness
     */
    public static void saveBrightness(Activity activity, int brightness) {
        ContentResolver resolver = activity.getContentResolver();
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");

        android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        resolver.notifyChange(uri, null);
    }

    /**
     * 设置屏幕亮度
     * @param activity
     * @param value
     */
    public static void setScreenBrightness(Activity activity, int value) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }
}
