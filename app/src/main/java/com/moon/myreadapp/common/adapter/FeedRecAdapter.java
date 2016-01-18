package com.moon.myreadapp.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvFeedItemBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.ScreenUtils;

import java.util.List;

/**
 * Created by moon on 15/11/9.
 */
public class FeedRecAdapter extends BaseRecyclerAdapter<Feed, LvFeedItemBinding> {


    public FeedRecAdapter(Context context, List<Feed> data) {
        super(context,data);
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

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int padding = ScreenUtils.getResources().getDimensionPixelOffset(R.dimen.normal_padding);
//        lp.setMargins(padding, padding, padding, 0);
        convertView.setLayoutParams(lp);
        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }

    @Override
    protected void onBindCoreViewHolder(final BindingHolder<LvFeedItemBinding> holder, int truePos) {
        Feed feed = mData.get(truePos);
        //XLog.d("feed:" + feed.toString());
        holder.getBinding().setFeed(feed);
        String icon = feed.getIcon();
        XLog.d("unReadCount:" + icon);
        if (icon != null) {
            // holder.getBinding().feedIcon.set(Uri.parse(icon));
            /**
             * 使用volley进行图片加载.首先使用cache,无cache再用网络
             */
            RequestQueue queue = Volley.newRequestQueue(holder.getBinding().feedIcon.getContext());

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
                    holder.getBinding().feedIcon.setImageResource(R.drawable.button_corners_unable);
                }
            });
            queue.add(imageRequest);
        }
    }


}
