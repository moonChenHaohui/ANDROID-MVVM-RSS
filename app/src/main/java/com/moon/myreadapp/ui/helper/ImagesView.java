package com.moon.myreadapp.ui.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.util.ImageHelper;

import java.util.ArrayList;

/**
 * Created by moon on 15/12/5.
 * 显示图片的线性布局
 */
public class ImagesView extends LinearLayout{

    private int imageLength = 1;
    private static String TAG = ImagesView.class.getSimpleName();


    public ImagesView(Context context) {
        super(context);
    }

    public ImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageLinearLayout);
        imageLength = a.getColor(R.styleable.ImageLinearLayout_imageMaxSize, imageLength);
        a.recycle();//资源回收
    }

    public void initChildren(){
        if (getChildCount() != 0){
            XLog.d(TAG + " init faild ! the children already added !");
            return;
        }
        float padding = getContext().getResources().getDimension(R.dimen.normal_half_padding);


        LayoutParams mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
        for (int i = 0; i < imageLength;i++){
            SimpleDraweeView sd = new SimpleDraweeView(getContext());
            ImageHelper.initImage(sd);
            if (i < imageLength - 1){
                sd.setPadding(0,0,(int)padding,0);
            }
            addView(sd, mLayoutParams);
        }
       // XLog.d(TAG + "init views success!count is :" + imageLength + ",each view size: " + width);
    }

    public void setImages(ArrayList<String> images){
        initChildren();
        if (images == null || images.size() == 0){
            //隐藏所有
            for (int i = 0;i < getChildCount();i++){
                getChildAt(i).setVisibility(INVISIBLE);
            }
            return;
        }
        int size = images.size();
        for (int i = size;i < getChildCount();i++){
            getChildAt(i).setVisibility(INVISIBLE);
        }
        XLog.d(TAG + " get images size :" + size);
        for(int i = 0;i < size;i++){
            SimpleDraweeView sd = (SimpleDraweeView)getChildAt(i);
            sd.setVisibility(VISIBLE);
            sd.setImageURI(Uri.parse(images.get(i)));
            XLog.d(TAG + "set image :" + images.get(i));
        }
        requestLayout();
    }
}
