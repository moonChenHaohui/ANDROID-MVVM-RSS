package com.moon.myreadapp.application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;

/**
 * Created by moon on 15/10/1.
 */
public class ReadApplication extends XApplication{

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
            return null;
        }

        @Override
        protected void onSafePostExecute(String s) {
            super.onSafePostExecute(s);
        }

    }
}
