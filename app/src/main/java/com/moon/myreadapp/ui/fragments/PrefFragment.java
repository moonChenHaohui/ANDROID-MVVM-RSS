package com.moon.myreadapp.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.Globals;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

/**
 * Created by moon on 15/10/25.
 */
public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    public static final String TAG = PrefFragment.class.getSimpleName();
    public static String PREFRENCE_NAME = "SETTING";


    private Preference cleanCache;
    private Preference updateVersion;
    private boolean inClaen;
    private boolean inUpdate;
    private Preference aboutAuthor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置名字要在add之前
        getPreferenceManager().setSharedPreferencesName(PREFRENCE_NAME);
        addPreferencesFromResource(R.xml.preferences);


        cleanCache = findPreference(getString(R.string.set_clean_cache_key));
        cleanCache.setOnPreferenceClickListener(this);
        updateVersion = findPreference(getString(R.string.set_update_version_key));
        updateVersion.setOnPreferenceClickListener(this);
        aboutAuthor = findPreference(getString(R.string.set_about_author_key));
        aboutAuthor.setOnPreferenceClickListener(this);
        initData();
    }

    private void initData(){
        long size = DBHelper.getDAO().getArticleDao().count();
        cleanCache.setSummary(getString(R.string.set_clean_cache_summary, DBHelper.Query.getArticlesCountReadAndUnFavor()));
        updateVersion.setSummary(getString(R.string.set_update_version_summary, Globals.getVersionName()));
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(cleanCache.getKey())){
            //清除缓存
            if (!inClaen) {
                inClaen = true;
                DBHelper.Delete.deleteArticleReadAndUnFavor();
                int count = DBHelper.Query.getArticlesCountReadAndUnFavor();
                cleanCache.setSummary(getString(R.string.set_clean_cache_summary, count));
                ToastHelper.showToast(R.string.set_clean_cache_summary_down);
                //如果没有文章,则不需要再清除
                if (count > 0){
                    inClaen = false;
                }

            }
        } else if (key.equals(aboutAuthor.getKey())){
            //查看作者
            DialogFractory.createDialog(getActivity(), DialogFractory.Type.AboutMe).show();
        } else if (key.equals(updateVersion.getKey())){
            if (!inUpdate) {
                inUpdate = true;
                updateVersion.setTitle(R.string.set_update_version_title_loading);
                BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        updateVersion.setTitle(R.string.set_update_version_title);
                        inUpdate = false;
                        if (updateStatus == UpdateStatus.Yes) {//版本有更新

//                    } else if (updateStatus == UpdateStatus.No) {
//                        Toast.makeText(mView, "版本无更新", Toast.LENGTH_SHORT).show();
//                    } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
//                        Toast.makeText(mView, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
//                    } else if (updateStatus == UpdateStatus.IGNORED) {
//                        Toast.makeText(mView, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
//                    } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
//                        Toast.makeText(mView, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
//                    } else if (updateStatus == UpdateStatus.TimeOut) {
//                        Toast.makeText(getContext(), "查询出错或查询超时", Toast.LENGTH_SHORT).show();
//                    }
                        } else {
                            ToastHelper.showToast(R.string.request_error);
                        }
                    }
                });

                BmobUpdateAgent.forceUpdate(getActivity());
            }
        }
        return false;
    }
}
