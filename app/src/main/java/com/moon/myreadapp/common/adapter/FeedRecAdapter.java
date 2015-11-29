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
    protected int getItemCoreViewType(int truePos) {
        return 1;
    }

    @Override
    protected BindingHolder<LvFeedItemBinding> onCreateCoreViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.lv_feed_item, null);
        LvFeedItemBinding binding = LvFeedItemBinding.bind(convertView);
        convertView.setTag(binding);


        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }

    @Override
    protected void onBindCoreViewHolder(BindingHolder<LvFeedItemBinding> holder, int truePos) {
        Feed feed = mData.get(truePos);
        holder.getBinding().setFeed(feed);
       // holder.getBinding().feedIcon.setImageURI(Uri.parse("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4168775445,1420260708&fm=116&gp=0.jpg"));
    }
}
