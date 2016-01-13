package com.moon.myreadapp.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.util.PreferenceUtils;

/**
 * Created by moon on 15/10/25.
 */
public class PrefFragment extends PreferenceFragment {

    public static final String TAG = PrefFragment.class.getSimpleName();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().setSharedPreferencesName(PreferenceUtils.PREFRENCE_NAME);


    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        XLog.d(TAG + "preferenceScreen: " + preferenceScreen.getTitle() + ",preference" + preference.getTitle());
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
