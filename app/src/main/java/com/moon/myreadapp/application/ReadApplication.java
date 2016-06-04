package com.moon.myreadapp.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.moon.appframework.common.util.SafeAsyncTask;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.DaoMaster;
import com.moon.myreadapp.mvvm.models.dao.DaoSession;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.HtmlHelper;
import com.moon.myreadapp.util.PreferenceUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by moon on 15/10/1.
 */
public class ReadApplication extends XApplication{


    private volatile DaoSession daoSession;

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

            //Bmob init
            Bmob.initialize(ReadApplication.this, Constants.APP_ID);

            //init start date
            ReadApplication.this.initBeginData();
            return null;
        }

        @Override
        protected void onSafePostExecute(String s) {
            super.onSafePostExecute(s);
        }
    }

    public DaoSession getDaoSession() {
        if (null == daoSession){
            synchronized (DaoSession.class) {
                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), Constants.DB_NAME, null);
                SQLiteDatabase db = helper.getWritableDatabase();
                DaoMaster daoMaster = new DaoMaster(db);
                daoSession = daoMaster.newSession();
            }
        }
        return daoSession;
    }
    private void initBeginData(){
        if (PreferenceUtils.getInstance(this).getBooleanParam(Constants.APP_IS_FIRST_USE, true)){//frist into
            DBHelper.Insert.feed(new Feed(1l,
                    "知乎每日精选",
                    "http://www.zhihu.com/rss",
                    0,
                    0,
                    "一个真实的网络问答社区，帮助你寻找答案，分享知识",
                    "",
                    "http://www.zhihu.com",
                    HtmlHelper.getIconUrlString("http://www.zhihu.com"),//icon获取
                    null,
                    null,
                    null,//最近的图片
                    null,
                    null,
                    null,
                    null,
                    DBHelper.Query.getUserId()));
            DBHelper.Insert.feed(new Feed(2l,
                    "36氪",
                    "http://www.36kr.com/feed",
                    0,
                    0,
                    "36氪，让创业更简单",
                    "",
                    "http://36kr.com",
                    HtmlHelper.getIconUrlString("http://www.36kr.com"),//icon获取
                    null,
                    null,
                    null,//最近的图片
                    null,
                    null,
                    null,
                    null,
                    DBHelper.Query.getUserId()));
        }
    }
}
