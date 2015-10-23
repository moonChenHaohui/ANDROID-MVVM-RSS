package com.moon.myreadapp.common.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.databinding.LeftDrawerListItemBinding;
import com.moon.myreadapp.mvvm.models.MenuItem;

import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerAdapter extends BaseListAdapter<MenuItem> {

    public DrawerAdapter(List<MenuItem> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftDrawerListItemBinding binding;
        if (convertView == null){
            binding = DataBindingUtil.inflate(mInflater,R.layout.left_drawer_list_item,parent,false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        binding = (LeftDrawerListItemBinding)convertView.getTag();


        binding.setItem(getmData().get(position));

        return convertView;
    }

}
