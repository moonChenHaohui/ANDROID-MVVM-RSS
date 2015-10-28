package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.ActivitySettingBinding;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.ui.fragments.PrefFragment;

public class SettingActivity extends BaseActivity {


    private ActivitySettingBinding binding;

    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        initToolBar(toolbar);
        PrefFragment prefFragment = new PrefFragment();
        getFragmentManager().beginTransaction().replace(R.id.setting_content, prefFragment).commit();
    }

    @Override
    protected Toolbar getTooBar() {
        return null;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_setting;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        this.binding = DataBindingUtil.setContentView(this,getLayoutView());
    }
}
