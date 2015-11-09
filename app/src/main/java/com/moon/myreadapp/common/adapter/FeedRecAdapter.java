package com.moon.myreadapp.common.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvFeedItemBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;

import java.util.List;

/**
 * Created by moon on 15/11/9.
 */
public class FeedRecAdapter extends BaseRecyclerAdapter<Feed,LvFeedItemBinding> {

    public FeedRecAdapter(List<Feed> data) {
        super(data);
    }

    @Override
    public BaseRecyclerAdapter.BindingHolder<LvFeedItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = mInflater.inflate(R.layout.lv_feed_item, null);
        LvFeedItemBinding binding = LvFeedItemBinding.bind(convertView);
        convertView.setTag(binding);


        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.BindingHolder<LvFeedItemBinding> holder, int position) {
        Feed feed = mData.get(position);

        holder.getBinding().setFeed(feed);
        holder.getBinding().feedIcon.setImageURI(Uri.parse("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4168775445,1420260708&fm=116&gp=0.jpg"));
    }
}
