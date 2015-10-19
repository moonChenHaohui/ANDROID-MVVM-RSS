package com.moon.myreadapp.read.homepage.viewhelper;

import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moon.appframework.common.util.StringUtils;
import com.moon.myreadapp.R;
import com.moon.myreadapp.read.homepage.business.bean.Jock;
import com.moon.myreadapp.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/10/4.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<Jock> model;
    private static int MAX_SHORT_TEXT_COUNT = 30;

    public ListViewAdapter (){
        model = new ArrayList<>();
    }

    public void setModel(List<Jock> model) {
        this.model = model;
        notifyDataSetChanged();
    }

    public void addModel(List<Jock> model) {
        this.model.addAll(model);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(Globals.getApplication()).inflate(R.layout.listview_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.getAuthor().setText(model.get(position).getAuthor());
        final String content = model.get(position).getContent();
        //设置文字收起
        if (StringUtils.isNotEmpty(content) && content.length() > MAX_SHORT_TEXT_COUNT){
            holder.getExpandBtn().setText("全文");
            holder.getExpandBtn().setVisibility(View.VISIBLE);
            holder.getContent().setText(content.substring(0,MAX_SHORT_TEXT_COUNT));
            holder.getExpandBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == v.getTag()){
                        v.setTag(1);
                    }
                    int flog_int = (int )v.getTag();
                    if (flog_int == 1){
                        ((TextView)v).setText("收起");
                        holder.getContent().setText(content);
                        v.setTag(0);
                    } else {
                        ((TextView)v).setText("全文");
                        holder.getContent().setText(content.substring(0,MAX_SHORT_TEXT_COUNT));
                        v.setTag(1);
                    }

                }
            });
        } else {
            if (holder.getExpandBtn().getVisibility() == View.VISIBLE){
                holder.getExpandBtn().setVisibility(View.GONE);
            }
            holder.getContent().setText(content);
        }
        String url = model.get(position).getPicUrl();
        if (StringUtils.isNotEmpty(url)){
            holder.getImage().setVisibility(View.VISIBLE);
            holder.getImage().setImageURI(Uri.parse(url));
            holder.getImage().setAspectRatio(1.33f);
        } else {
            holder.getImage().setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        private TextView author;
        private TextView content;
        private TextView expandBtn;
        private SimpleDraweeView image;

        public ViewHolder (View view){
            author = (TextView)view.findViewById(R.id.author);
            content = (TextView)view.findViewById(R.id.content);
            image = (SimpleDraweeView)view.findViewById(R.id.image);
            expandBtn = (TextView)view.findViewById(R.id.expanded_btn);
        }

        public TextView getAuthor() {
            return author;
        }

        public void setAuthor(TextView author) {
            this.author = author;
        }

        public TextView getContent() {
            return content;
        }

        public void setContent(TextView content) {
            this.content = content;
        }

        public SimpleDraweeView getImage() {
            return image;
        }

        public void setImage(SimpleDraweeView image) {
            this.image = image;
        }

        public TextView getExpandBtn() {
            return expandBtn;
        }

        public void setExpandBtn(TextView expandBtn) {
            this.expandBtn = expandBtn;
        }
    }
}
