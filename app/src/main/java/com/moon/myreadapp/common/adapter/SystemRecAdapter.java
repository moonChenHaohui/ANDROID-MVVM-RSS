package com.moon.myreadapp.common.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvFeedItemBinding;
import com.moon.myreadapp.databinding.LvRecommendBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;

import java.util.List;

/**
 * Created by moon on 15/12/17.
 */
public class SystemRecAdapter extends BaseRecyclerAdapter<Feed,LvRecommendBinding> {

    public SystemRecAdapter(List<Feed> data) {
        super(data);
    }

    @Override
    protected int getItemCoreViewType(int truePos) {
        return 1;
    }

    @Override
    protected void onBindCoreViewHolder(BindingHolder<LvRecommendBinding> holder, int truePos) {
        Feed feed = getmData().get(truePos);
        holder.getBinding().setTitle(feed.getTitle());
    }

    @Override
    protected BindingHolder<LvRecommendBinding> onCreateCoreViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.lv_recommend, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,convertView.getResources().getDimensionPixelSize(R.dimen.normal_padding),0,0);
        convertView.setLayoutParams(lp);

        LvRecommendBinding binding = LvRecommendBinding.bind(convertView);
        convertView.setTag(binding);
        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }
}
