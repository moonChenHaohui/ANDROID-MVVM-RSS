package com.moon.myreadapp.common.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.common.adapter.base.FeedAdapterHelper;
import com.moon.myreadapp.common.components.pinnedsectionlistView.PinnedSectionListView;
import com.moon.myreadapp.databinding.LvFeedItemBinding;
import com.moon.myreadapp.mvvm.models.Feed;
import com.moon.myreadapp.mvvm.models.ListFeed;

import java.net.URI;
import java.util.List;

/**
 * Created by moon on 15/10/23.
 */
public class FeedAdapter extends BaseListAdapter<ListFeed> implements PinnedSectionListView.PinnedSectionListAdapter {

    public FeedAdapter(List<ListFeed> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TextView view = (TextView) super.getView(position, convertView, parent);

        if (null == convertView) {
            FeedAdapterHelper.TimeType itemType = getItem(position).tpye;
            if (itemType == FeedAdapterHelper.TimeType.TODATY) {
                convertView = mInflater.inflate(R.layout.lv_feed_divider_header, parent, false);
            } else if (itemType == FeedAdapterHelper.TimeType.YESTORY) {
                LvFeedItemBinding binding = DataBindingUtil.inflate(mInflater,R.layout.lv_feed_item,parent,false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            }
        }
        if (convertView.getTag() instanceof LvFeedItemBinding){
            LvFeedItemBinding binding = (LvFeedItemBinding)convertView.getTag();
            binding.channelIcon.setImageURI(Uri.parse("http://www.logoquan.com/upload/list/20150925/logoquan14509819713.PNG"));
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return FeedAdapterHelper.TimeType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).tpye.getId();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == FeedAdapterHelper.TimeType.TODATY.getId();
    }

}