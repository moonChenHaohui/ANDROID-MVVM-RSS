package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.FragmentFeedSetBinding;
import com.moon.myreadapp.databinding.FragmentReadSetBinding;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ScreenUtils;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Switch;

/**
 * Created by moon on 16/01/07.
 */
public class FeedSetDialog extends BaseButtomDialog implements View.OnClickListener {

    private FragmentFeedSetBinding binding;
    private TextFont textFont;
    private static boolean showAllArticles = true;

    public FeedSetDialog(Activity context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commit) {
            dismiss();
            return;
        }
    }

    @Override
    void setContentView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_feed_set, null, true);
        setContentView(binding.getRoot());
    }


    @Override
    void init() {

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = binding.shareContent.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        binding.commit.setOnClickListener(this);

        showAllArticles = PreferenceUtils.getInstance(context).getBooleanParam(Constants.FEED_SHOW_ALL, showAllArticles);
        binding.readSwitch.setChecked(showAllArticles);
        binding.readSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                showAllArticles = checked;
                UpdateFeedEvent event = new UpdateFeedEvent(null, UpdateFeedEvent.TYPE.SET);
                event.setShowAllArticles(showAllArticles);
                XApplication.getInstance().bus.post(event);

                //做一个延迟
                binding.readSwitch.setEnabled(false);
                binding.readSwitch.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.readSwitch.setEnabled(true);
                    }
                }, 1000);
            }
        });

        //初始化


    }

    @Override
    void onDimiss() {
        //保存
        PreferenceUtils.getInstance(context).saveParam(Constants.FEED_SHOW_ALL, showAllArticles);
    }

}
