package com.moon.myreadapp.common.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.databinding.LvChannelItemBinding;
import com.moon.myreadapp.mvvm.models.Channel;

import java.util.List;

/**
 * Created by moon on 15/10/20.
 */
public class ReadAdapter extends BaseListAdapter<Channel> {


    public ReadAdapter(List<Channel> data) {
        super(data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LvChannelItemBinding binding;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.lv_channel_item, null);
            binding = LvChannelItemBinding.bind(convertView);convertView.setTag(binding);
        }
        binding = (LvChannelItemBinding)convertView.getTag();

        binding.setChannel(getmData().get(position));
        binding.channelIcon.setImageURI(Uri.parse("http://www.logoquan.com/upload/list/20150925/logoquan14453570736.PNG"));
        return convertView;
    }
}
