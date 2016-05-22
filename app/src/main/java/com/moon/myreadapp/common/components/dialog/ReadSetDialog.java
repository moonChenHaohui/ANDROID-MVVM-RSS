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
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.FragmentReadSetBinding;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ScreenUtils;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Switch;

/**
 * Created by moon on 16/01/02.
 */
public class ReadSetDialog extends BaseButtomDialog implements View.OnClickListener {

    private FragmentReadSetBinding binding;
    private TextFont textFont;

    public ReadSetDialog(Activity context) {
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_read_set, null, true);
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

        //初始化

        //亮度设置
        binding.lightSlider.setValue(ScreenUtils.getScreenBrightness(context), true);
        binding.lightSlider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                ScreenUtils.setScreenBrightness(context, newValue);
                //XLog.d("Slider view, boolean fromUser," + fromUser + " float oldPos" + oldPos + ", float newPos" + newPos + ", int oldValue" + oldValue + ", int newValue" + newValue + "");
            }
        });
//        //夜间模式开关
//        binding.modeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(Switch view, boolean checked) {
//                //ToastHelper.showToast("夜间模式:" + checked);
//            }
//        });

        //字体设置
        //binding.fontSlider.setValue();
        //从配置中获取
        int fontSize = PreferenceUtils.getInstance(context).getIntParam(Constants.ARTICLE_FONT_SIZE, (int)Globals.getApplication().getResources().getDimension(TextFont.H3.size));
        //转换成TextFont
        textFont = TextFont.findBySize(fontSize);
        binding.fontSlider.setValue(textFont.level,true);
        binding.fontText.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)Globals.getApplication().getResources().getDimension(textFont.size));
        binding.fontSlider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                //XLog.d("Slider view, boolean fromUser," + fromUser + " float oldPos" + oldPos + ", float newPos" + newPos + ", int oldValue" + oldValue + ", int newValue" + newValue + "");
                TextFont newFont = TextFont.findByLevel(newValue);
                int size = context.getResources().getDimensionPixelSize(newFont.size);
                //setTextSize 的坑,是默认设置sp.需要强制设置一下px模式
                binding.fontText.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
                XApplication.getInstance().bus.post(new UpdateArticleEvent(size));
                textFont = newFont;
                PreferenceUtils.getInstance(context).saveParam(Constants.ARTICLE_FONT_SIZE,(int)Globals.getApplication().getResources().getDimension(textFont.size));
            }
        });

    }

    @Override
    void onDimiss() {
        //保存修改的亮度
        ScreenUtils.saveBrightness(context,binding.lightSlider.getValue());
        //保存字体修改u
        PreferenceUtils.getInstance(context).saveParam(Constants.ARTICLE_FONT_SIZE,(int)Globals.getApplication().getResources().getDimension(textFont.size));
    }

}
