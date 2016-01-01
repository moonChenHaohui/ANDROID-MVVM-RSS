package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.SimpleToastHelper;
import com.moon.myreadapp.mvvm.models.ShareItem;

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

        GridView gv = (GridView)view.findViewById(R.id.gridview);
        final ShareAdapter adapter = new ShareAdapter(context);
        String[] array = context.getResources().getStringArray(R.array.share_dialog_text);
        adapter.add(new ShareItem.Builder(context)
                .content(array[0])
                .icon(R.drawable.ic_wx_logo)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[1])
                .icon(R.drawable.ic_wx_moments)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[2])
                .icon(R.drawable.ic_wx_collect)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[3])
                .icon(R.drawable.ic_sina_logo)
                .build());
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleToastHelper.showToast("click position:" + position);
//                switch (position) {
//                    case 0:
//                        shareToWeChatSession();
//                        break;
//                    case 1:
//                        shareToWeChatTimeline();
//                        break;
//                    case 2:
//                        shareToWeChatFavorite();
//                        break;
//                    case 3:
//                        shareToWeibo();
//                        break;
//                    default:
//                        share("", null);
//                }
            }
        });
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


    class ShareAdapter extends ArrayAdapter<ShareItem>{


        public ShareAdapter(Context context) {
            super(context, R.layout.gv_share_item, R.id.name);
        }
        @Override
        public View getView(final int index, View convertView, ViewGroup parent) {
            final View view = super.getView(index, convertView, parent);
            final ShareItem item = getItem(index);
            DraweeView ic = (DraweeView) view.findViewById(R.id.image);
            if (item.getIcon() != 0)
                ic.setImageResource(item.getIcon());
            else
                ic.setVisibility(View.GONE);
            IconTextView tv = (IconTextView) view.findViewById(R.id.name);
            tv.setText(item.getContent());
            return view;
        }
    }




}
