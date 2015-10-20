package com.moon.myreadapp.util;

import android.content.Context;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;

import net.steamcrafted.loadtoast.LoadToast;

/**
 * Created by moon on 15/10/19.
 * 提供统一的toast接口
 */
public class ToastUtil{

    private static LoadToast loadToast;
    /**
     * 返回默认的的loadtoast
     * @param context
     * @return
     */
    public static LoadToast createToast(Context context,String str){
        if (loadToast == null){
            loadToast = new LoadToast(context).
                    setTranslationY(ScreenUtils.getDisplayMetrics().heightPixels / 2).
                    setText(str).
                    setBackgroundColor(ScreenUtils.getResources().getColor(R.color.toast_background)).
                    setProgressColor(ScreenUtils.getResources().getColor(R.color.toast_progress)).
                    setTextColor(ScreenUtils.getResources().getColor(R.color.toast_text));
        }
        return loadToast;
    }

}
