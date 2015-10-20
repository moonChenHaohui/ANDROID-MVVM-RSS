package com.moon.myreadapp.common.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.databinding.LeftDrawerListItemBinding;
import com.moon.myreadapp.mvvm.models.BaseMenuItem;

import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerAdapter extends BaseListAdapter<BaseMenuItem> {

    public DrawerAdapter(Context contex, List<BaseMenuItem> data) {
        super(contex, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // LeftDrawerListItemBinding binding = LeftDrawerListItemBinding.inflate(mInflater, parent,false);
        //TODO 使用data binding来操作
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.left_drawer_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(getmData().get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        TextView title;
    }
}
