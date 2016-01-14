package com.moon.myreadapp.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.Globals;

/**
 * Created by moon on 15/10/25.
 */
public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    public static final String TAG = PrefFragment.class.getSimpleName();
    public static String PREFRENCE_NAME = "SETTING";


    private Preference cleanCache;
    private boolean inClaen;
    private Preference aboutAuthor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置名字要在add之前
        getPreferenceManager().setSharedPreferencesName(PREFRENCE_NAME);
        addPreferencesFromResource(R.xml.preferences);


        cleanCache = findPreference(getString(R.string.set_clean_cache_key));
        cleanCache.setOnPreferenceClickListener(this);
        aboutAuthor = findPreference(getString(R.string.set_about_author_key));
        aboutAuthor.setOnPreferenceClickListener(this);
        initData();
    }

    private void initData(){
        long size = DBHelper.getDAO().getArticleDao().count();
        cleanCache.setSummary(getString(R.string.set_clean_cache_summary, DBHelper.Query.getArticlesCountReadedAndUnFavor()));
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(cleanCache.getKey())){
            //清除缓存
            if (!inClaen) {
                inClaen = true;
                DBHelper.Delete.deleteArticleReadedAndUnFavor();
                int count = DBHelper.Query.getArticlesCountReadedAndUnFavor();
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
        }
        return false;
    }
}
