package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.FragmentAboutMeBinding;
import com.moon.myreadapp.databinding.FragmentFeedSetBinding;
import com.moon.myreadapp.util.PreferenceUtils;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Switch;

/**
 * Created by moon on 16/01/07.
 */
public class AboutMeDialog extends Dialog {

    private FragmentAboutMeBinding binding;

    public AboutMeDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutParams(-1, -2);
        canceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_about_me, null, false);
        setContentView(binding.getRoot());
        binding.aboutMeLink.setText(Html.fromHtml(getContext().getString(R.string.about_me_link)));
        binding.aboutMeLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
