package com.moon.myreadapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.FeedAdapterHelper;
import com.moon.myreadapp.common.components.pinnedsectionlistView.PinnedSectionListView;
import com.moon.myreadapp.mvvm.models.Feed;
import com.moon.myreadapp.mvvm.models.ListFeed;

/**
 * Created by moon on 15/10/23.
 */
public class FeedAdapter extends ArrayAdapter<ListFeed> implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int[] COLORS = new int[]{
            R.color.pink, R.color.blue,
            R.color.green, R.color.grey};

    public FeedAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        generateDataset('A', 'C', false);
    }

    public void generateDataset(char from, char to, boolean clear) {

        if (clear) clear();

        final int sectionsNumber = to - from + 1;
        int sectionPosition = 0, listPosition = 0;
        for (char i = 0; i < sectionsNumber; i++) {
            ListFeed section = new ListFeed(new Feed());
            add(section);

            final int itemsNumber = (int) Math.abs((Math.cos(2f * Math.PI / 3f * sectionsNumber / (i + 1f)) * 25f));
            for (int j = 0; j < itemsNumber; j++) {
                ListFeed item = new ListFeed(new Feed("2"));

                item.sectionPosition = sectionPosition;
                item.listPosition = listPosition++;
                add(item);
            }

            sectionPosition++;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TextView view = (TextView) super.getView(position, convertView, parent);

        if (null == convertView) {
            FeedAdapterHelper.TimeType itemType = getItem(position).tpye;
            if (itemType == FeedAdapterHelper.TimeType.TODATY) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_feed_divider_header, parent, false);
            } else if (itemType == FeedAdapterHelper.TimeType.YESTORY) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_channel_item, parent, false);
            }

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