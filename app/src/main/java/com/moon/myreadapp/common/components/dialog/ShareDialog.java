package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.moon.myreadapp.R;

/**
 * Created by moon on 15/12/24.
 */
public class ShareDialog extends PopupWindow {

    private Activity context;
    private Window mWindow;

    public ShareDialog(Activity context) {
        this.context = context;
        this.mWindow = context.getWindow();

        final View view = LinearLayout.inflate(context, R.layout.fragment_share, null);
        setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.ButtomPopupAnimStyle);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.share_content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mWindow.getAttributes();
//                lp.width+=200;
//                lp.height+=200;
                lp.alpha = 1f;
                mWindow.setAttributes(lp);
            }
        });

    }

    public void showWithView(View v) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        //TODO some thing
        lp.alpha = 0.4f;
        mWindow.setAttributes(lp);
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}
