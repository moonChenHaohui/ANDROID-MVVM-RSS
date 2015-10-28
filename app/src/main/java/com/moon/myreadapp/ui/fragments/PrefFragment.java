package com.moon.myreadapp.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.moon.myreadapp.R;

/**
 * Created by moon on 15/10/25.
 */
public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
