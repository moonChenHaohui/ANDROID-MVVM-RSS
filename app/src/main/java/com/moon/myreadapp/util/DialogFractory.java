package com.moon.myreadapp.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.myreadapp.R;
import com.moon.myreadapp.ui.helper.SubDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.app.Dialog;

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
        FontSet
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
