package com.moon.myreadapp.util;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;

import com.moon.myreadapp.application.ReadApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by moon on 15/10/1.
 */
public class Globals {
    private static Application sApplication;
    private static ClassLoader sClassLoader;
    private static String sInstalledVersionName;

    public Globals() {
    }

    public static synchronized Application getApplication() {
        if(sApplication == null) {
            sApplication = getSystemApp();
        }

        return sApplication;
    }

    public static synchronized ClassLoader getClassLoader() {
        if(sClassLoader == null) {
            Application app = getApplication();
            return app.getClassLoader();
        } else {
            return sClassLoader;
        }
    }

    private static Application getSystemApp() {
        return ReadApplication.getInstance();
        /*
        try {
            Class e = Class.forName("android.app.ActivityThread");
            Method m_currentActivityThread = e.getDeclaredMethod("currentActivityThread");
            Field f_mInitialApplication = e.getDeclaredField("mInitialApplication");
            f_mInitialApplication.setAccessible(true);
            Object current = m_currentActivityThread.invoke(null);
            Object app = f_mInitialApplication.get(current);
            return (Application)app;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
        */
    }

    public static String getVersionName() {
        try {
            return getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
        } catch (NameNotFoundException var1) {
            var1.printStackTrace();
            return "1.0.0";
        }
    }

    public static String getInstalledVersionName() {
        return sInstalledVersionName;
    }

    public static int getVersionCode() {
        String packageName = getApplication().getPackageName();
        int versionCode = 0;

        try {
            versionCode = getApplication().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return versionCode;
    }

}
