package com.moon.myreadapp.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.common.components.toast.TastyToast;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.databinding.LvRecommendBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.DBHelper;

import java.util.List;

/**
 * Created by moon on 15/12/17.
 */
public class SystemRecAdapter extends BaseRecyclerAdapter<Feed, LvRecommendBinding> {

    private View.OnClickListener listener;
    private static int SUB_STATUS_SUBED = 1;
    private static int SUB_STATUS_UNSUB = 0;


    public SystemRecAdapter(final Context context,final List<Feed> data) {
        super(context,data);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (v instanceof IconTextView) {
                    final IconTextView view = (IconTextView) v;
                    final int pos;
                    int status;
                    try{
                        pos = (int) v.getTag(R.id.tag_sub_position);
                        status = (int) v.getTag(R.id.tag_sub_status);

                    }catch (Exception e){
                        return;
                    }

                    //XLog.d("pos;" + pos + ";status:"+ status);
                    if (pos >= 0 && pos<getmData().size()){
                        view.setText(v.getResources().getString(R.string.dialog_sub_search_load));
                        view.setEnabled(false);
                        if (status ==SUB_STATUS_UNSUB){

                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DBHelper.Insert.feed(getmData().get(pos));
                                    updateFeed();
                                    ToastHelper.showNotice((Activity) context, view.getResources().getString(R.string.sub_notice_already_join, getmData().get(pos).getTitle()), TastyToast.STYLE_MESSAGE).setDuration(1000);
                                    view.setText(view.getResources().getString(R.string.sub_btn_cancel_sub));
                                    view.setEnabled(true);
                                }
                            }, 500);
                            view.setTag(R.id.tag_sub_status, SUB_STATUS_SUBED);
                        } else {

                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.setEnabled(false);
                                    DBHelper.Delete.deleteFeed(getmData().get(pos));
                                    updateFeed();
                                    view.setText(view.getResources().getString(R.string.sub_btn_sub));
                                    view.setEnabled(true);
                                    view.setTag(R.id.tag_sub_status, SUB_STATUS_UNSUB);
                                }
                            },500);
                        }
                    }
                }
            }
        };
    }

    @Override
    protected int getItemCoreViewType(int truePos) {
        return 1;
    }

    @Override
    protected void onBindCoreViewHolder(final BindingHolder<LvRecommendBinding> holder,
                                        int truePos) {
        Feed feed = getmData().get(truePos);
        holder.getBinding().setFeed(feed);

        //记录一下tag,绑定listener
        holder.getBinding().sub.setTag(R.id.tag_sub_position, truePos);
        holder.getBinding().sub.setOnClickListener(listener);

        //查询本地是否已经订阅
        if (DBHelper.Query.findFeedByURL(feed.getUrl()) == null) {
            //未订阅
            holder.getBinding().sub.setTag(R.id.tag_sub_status, SUB_STATUS_UNSUB);
            holder.getBinding().sub.setText( holder.getBinding().sub.getResources().getString(R.string.sub_btn_sub));
        } else {
            //已订阅
            holder.getBinding().sub.setTag(R.id.tag_sub_status, SUB_STATUS_SUBED);
            holder.getBinding().sub.setText( holder.getBinding().sub.getResources().getString(R.string.sub_btn_cancel_sub));
        }

        //加载icon
        if (StringUtils.isNotEmpty(feed.getIcon())) {
            RequestQueue queue = Volley.newRequestQueue(holder.getBinding().feedIcon.getContext());

            ImageRequest imageRequest = new ImageRequest(
                    feed.getIcon(),
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

    @Override
    protected BindingHolder<LvRecommendBinding> onCreateCoreViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View convertView = mInflater.inflate(R.layout.lv_recommend, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        convertView.setLayoutParams(lp);
        LvRecommendBinding binding = LvRecommendBinding.bind(convertView);
        convertView.setTag(binding);
        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }

    private void updateFeed() {
        XApplication.getInstance().bus.post(new UpdateFeedListEvent());
    }
}
