package com.moon.myreadapp.util;

import android.net.Uri;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moon.myreadapp.R;

/**
 * Created by moon on 15/12/5.
 */
public class ImageHelper {


    private static GenericDraweeHierarchyBuilder builder;

    private static GenericDraweeHierarchyBuilder getInstance(){
        if (null == builder){
            builder =
                    new GenericDraweeHierarchyBuilder(Globals.getApplication().getResources());
        }
        return builder;
    }

    public static void initImage(SimpleDraweeView simpleDraweeView){
        GenericDraweeHierarchy hierarchy = getInstance().build();
        //设置占位图
        hierarchy.setPlaceholderImage(R.drawable.image_bg);
        //中心缩放
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);

        simpleDraweeView.setHierarchy(hierarchy);

    }
}
