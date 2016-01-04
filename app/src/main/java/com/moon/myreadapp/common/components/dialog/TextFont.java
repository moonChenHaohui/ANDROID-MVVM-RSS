package com.moon.myreadapp.common.components.dialog;

/**
 * Created by moon on 16/1/4.
 */

import android.support.annotation.DimenRes;

import com.moon.myreadapp.R;
import com.moon.myreadapp.util.Globals;

public enum TextFont{
    H1(R.dimen.text_h1,5),
    H2(R.dimen.text_h2,4),
    H3(R.dimen.text_h3,3),
    H4(R.dimen.text_h4,2),
    H5(R.dimen.text_h5,1);

    @DimenRes
    public int size;
    public int level;
    TextFont(@DimenRes int s,int l){
        size = s;
        level = l;
    }
    public static TextFont findByLevel(int l){
        TextFont[] fonts = values();
        for (int i = 0;i < fonts.length;i++){
            if (fonts[i].level == l){
                return fonts[i];
            }
        }
        return H3;
    }
    public static TextFont findBySize(int size){
        TextFont[] fonts = values();
        for (int i = 0;i < fonts.length;i++){
            if (Globals.getApplication().getResources().getDimension(fonts[i].size) == size){
                return fonts[i];
            }
        }
        return H3;
    }
}