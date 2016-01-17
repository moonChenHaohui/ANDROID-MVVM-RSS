package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityWelcomeBinding;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.HtmlHelper;
import com.moon.myreadapp.util.PreferenceUtils;
import com.nineoldandroids.animation.ObjectAnimator;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;
    private int delayAnimTime = 1000;
    private int delayEntryTime = 1000;

    private boolean isFirstEntry = true;
    @Override
    protected void setContentViewAndBindVm(Bundle savedInstanceState) {
        isFirstEntry = isFirstEntry();
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutView(), null, false);
        setContentView(binding.getRoot());

    }

    @Override
    protected Toolbar getToolBar() {
        return null;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (binding == null) return;
        binding.appInfo.setText(Html.fromHtml(getString(R.string.welcome_app_info, getString(R.string.app_name), Globals.getVersionName())));
        binding.sayHi.postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示动画效果
                com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(binding.sayHi, "alpha", 0, 1).setDuration(500)

                );
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();
                binding.sayHi.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        XDispatcher.from(WelcomeActivity.this).dispatch(new RouterAction(MainActivity.class, true));
                    }
                }, delayEntryTime);
            }
        }, delayAnimTime);

    }

    private boolean isFirstEntry(){
        return PreferenceUtils.getInstance(this).getBooleanParam(Constants.APP_IS_FIRST_USE, true);
    }
}
