package com.moon.myreadapp.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.moon.myreadapp.R;
import com.moon.myreadapp.mvvm.models.MenuItem;

import java.lang.reflect.Method;

/**
 * Created by moon on 15/10/22.
 */
public class ViewUtils {


    /**
     * 获取焦点并弹出软键盘
     *
     * @param view
     */
    public static void editViewFocus(EditText view, boolean openKeyBord) {
        view.requestFocus();
        if (openKeyBord) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }


    public static Menu showPopupMenu(final Context context, View view, int layout, PopupMenu.OnMenuItemClickListener listener) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(context, view);

        // menu布局

        //4.0以上icon无法显示,需要反射调用该方法
        setIconEnable(popupMenu.getMenu(), true);
        popupMenu.getMenuInflater().inflate(layout, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(listener);
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(context, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
        return popupMenu.getMenu();
    }


    private static void setIconEnable(Menu menu, boolean enable) {

        try {
            //未知的类
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
