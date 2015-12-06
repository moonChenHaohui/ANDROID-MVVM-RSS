package com.moon.myreadapp.common.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvFeedItemBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;

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
    protected void onBindCoreViewHolder(final BindingHolder<LvFeedItemBinding> holder, int truePos) {
        Feed feed = mData.get(truePos);
        XLog.d("feed:" + feed.toString());
        holder.getBinding().setFeed(feed);
        String icon = feed.getIcon();
        if (icon == null){
            icon =  "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4168775445,1420260708&fm=116&gp=0.jpg";
        }
        /**
         * 使用volley进行图片加载
         */
        ImageRequest imageRequest = new ImageRequest(
                icon,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.getBinding().feedIcon.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                holder.getBinding().feedIcon.setImageResource(R.drawable.bg_empty_image);
            }
        });
        Volley.newRequestQueue(holder.getBinding().feedIcon.getContext()).add(imageRequest);
        //holder.getBinding().feedIcon.setImageURI(Uri.parse(icon));
    }
}
