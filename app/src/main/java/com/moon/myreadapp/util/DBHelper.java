package com.moon.myreadapp.util;

import com.moon.myreadapp.application.ReadApplication;
import com.moon.myreadapp.mvvm.models.dao.DaoSession;

/**
 * Created by moon on 15/11/9.
 */
public class DBHelper {

    public static synchronized DaoSession getDAO (){
        return ((ReadApplication)ReadApplication.getInstance()).getDaoSession();
    }
}
