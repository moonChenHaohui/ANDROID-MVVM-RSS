package com.moon.myreadapp.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by moon on 15/10/22.
 */
public class ViewUtils {


    /**
     * 获取焦点并弹出软键盘
     * @param view
     */
    public static void editViewFocus(EditText view,boolean openKeyBord){
        view.requestFocus();
        if (openKeyBord) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }
}
