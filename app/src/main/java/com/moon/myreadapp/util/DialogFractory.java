package com.moon.myreadapp.util;

import android.content.Context;
import android.widget.EditText;

import com.moon.myreadapp.ui.helper.SubDialog;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by moon on 15/10/22.
 */
public class DialogFractory {

    public enum Type {
        Toast,
        YesNo,
        InputValue,
        /**
         * 输入订阅源dialog
         */
        AddSubscrible,
    }

    public static MaterialDialog create(Context context, Type type) {
        MaterialDialog dialog = null;
        switch (type) {
            case YesNo:
            case InputValue:
            case AddSubscrible:
                dialog = new SubDialog(context);
                break;
                default:
                    EditText contentView = new EditText(context);
                    dialog = new MaterialDialog(context).setView(contentView);
        }
        return dialog;
    }
}
