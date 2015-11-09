package com.moon.myreadapp.application;

import android.database.sqlite.SQLiteDatabase;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.DaoMaster;
import com.moon.myreadapp.mvvm.models.dao.DaoSession;
import com.moon.myreadapp.mvvm.models.dao.UserDao;

/**
 * Created by moon on 15/10/1.
 */
public class ReadApplication extends XApplication{


    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        new AppInitTask().execute("start");
    }


    class AppInitTask extends SafeAsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            //iconify init
            Iconify.with(new FontAwesomeModule());
            // Fresco init
            Fresco.initialize(ReadApplication.this);


            return null;
        }

        @Override
        protected void onSafePostExecute(String s) {
            super.onSafePostExecute(s);
        }
    }

    public synchronized DaoSession getDaoSession() {
        if (null == daoSession){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), Constants.DB_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
