package com.moon.myreadapp.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.common.components.dialog.AboutMeDialog;
import com.moon.myreadapp.common.components.dialog.AddSubDialog;
import com.moon.myreadapp.common.components.dialog.RegisterDialog;
import com.moon.myreadapp.common.components.dialog.UserDialog;
import com.rey.material.app.Dialog;

import java.util.Arrays;
import java.util.List;

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
        FontSet,
        /**
         * 主题选择
         */
        ThemeChoose,
        /**
         * 加载
         */
        Loading,
        /**
         * 注册
         */
        Register,
        /**
         * 空页面
         */
        EmptyView,
        /**
         * 关于我
         */
        AboutMe,
        /**
         * 用户信息
         */
        UserInfo,
    }


    public static Dialog createDialog(final Context context, Type type) {
        final Dialog dialog;
        switch (type) {
            case ThemeChoose:
                dialog = new Dialog(context).
                        title(R.string.set_theme_picker_title).cancelable(true);
                Integer[] res = new Integer[]{R.color.red, R.color.brown, R.color.blue,
                        R.color.blue_grey, R.color.yellow, R.color.deep_purple,
                        R.color.pink, R.color.green};
                List<Integer> list = Arrays.asList(res);
                ColorAdapter adapter = new ColorAdapter(list);
                adapter.setCheckItem(ThemeUtils.getCurrentTheme(context).getIntValue());
                GridView gridView = (GridView) LayoutInflater.from(context).inflate(R.layout.fragment_theme_picker, null);
                gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gridView.setCacheColorHint(0);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int value = ThemeUtils.getCurrentTheme(context).getIntValue();
                        if (value != position) {
                            //mPreferenceUtils.saveParam(getString(mContext, R.string.change_theme_key), position);
                            PreferenceUtils.getInstance(context)
                                    .saveParam(context.getString(R.string.set_theme), position);
                        }
                        dialog.dismiss();
                        //切换当前act的主题
                        if (context instanceof Activity) {
                            ((Activity) context).recreate();
                        }
                    }
                });
                dialog.setContentView(gridView);
                break;
            case Loading:
                dialog = new Dialog(context).cancelable(false).canceledOnTouchOutside(false);
                View view = LayoutInflater.from(context).inflate(R.layout.fragment_loading, null);
                dialog.setContentView(view);
                break;
            case Register:
                dialog = new RegisterDialog(context);
                break;
            case AddSubscrible:
                dialog = new AddSubDialog(context);
                break;
            case EmptyView:
                dialog = new Dialog(context).cancelable(false).canceledOnTouchOutside(false).positiveAction(R.string.action_conform);
                break;
            case AboutMe:
                dialog = new AboutMeDialog(context);
                break;
            case UserInfo:
                dialog = new UserDialog(context);
                break;
            case YesNo:
                dialog = new Dialog(context) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        positiveAction(R.string.action_conform).
                                negativeAction(R.string.action_cancel).
                                negativeActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismiss();
                                    }
                                });
                    }
                };
                break;
            default:
                dialog = new Dialog(context);
        }
        return dialog;
    }

    static class ColorAdapter extends BaseListAdapter<Integer> {
        public ColorAdapter(List<Integer> data) {
            super(data);
        }

        private int checkItem = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.gv_color_item, null);
                holder = new Holder();
                holder.imageView1 = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
                holder.imageView2 = (ImageView) convertView.findViewById(R.id.img_2);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            GenericDraweeHierarchy hierarchy = holder.imageView1.getHierarchy();
            hierarchy.setPlaceholderImage(getmData().get(position));
            if (checkItem == position) {
                holder.imageView2.setImageResource(R.drawable.ic_check);
            }
            return convertView;
        }

        public int getCheckItem() {
            return checkItem;
        }

        public void setCheckItem(int checkItem) {
            this.checkItem = checkItem;
        }

        class Holder {
            SimpleDraweeView imageView1;
            ImageView imageView2;
        }
    }


}
