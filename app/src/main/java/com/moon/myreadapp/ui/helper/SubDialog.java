package com.moon.myreadapp.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.common.util.StringUtils;
import com.moon.myreadapp.R;
import com.moon.myreadapp.util.ScreenUtils;
import com.moon.myreadapp.util.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by moon on 15/10/22.
 * 添加订阅源的dialog
 */
public class SubDialog extends MaterialDialog {


    private MaterialEditText editText;

    private IconTextView load;
    public SubDialog(Context context,String url) {
        this(context);
        editText.setText(url);
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.editViewFocus(editText,true);
    }

    public SubDialog(Context context) {
        super(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_sub, null);
        setTitle("输入RSS Url");
        setContentView(view);
        editText = (MaterialEditText) view.findViewById(R.id.edit);
        load = (IconTextView)view.findViewById(R.id.load);

        setPositiveButton("搜索", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(editText.getText())) {
                    editText.setError("请输入正确的url");
                } else {
                    load.setVisibility(View.VISIBLE);
                    editText.setEnabled(false);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            load.setVisibility(View.GONE);
                            editText.setEnabled(true);
                            ViewUtils.editViewFocus(editText,false);
                            if (Math.random() < .5){
                                editText.setError("无法获取当前url的订阅源.");
                                ViewUtils.editViewFocus(editText, true);
                            }
                        }
                    }, 4000);
                }
            }
        });
        setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
