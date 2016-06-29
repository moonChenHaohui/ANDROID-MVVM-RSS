package com.moon.myreadapp.common.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvArticleItemNoImgBinding;
import com.moon.myreadapp.databinding.LvArticleItemOneImgBinding;
import com.moon.myreadapp.databinding.LvArticleItemThreeImgBinding;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.viewmodels.ViewArticleViewModel;
import com.moon.myreadapp.util.HtmlHelper;
import com.moon.myreadapp.util.ScreenUtils;
import com.moon.myreadapp.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/11/14.
 */
public class ArticleRecAdapter extends BaseRecyclerAdapter<Article, ViewDataBinding> {

    enum TYPE {
        /**
         * 没有图片
         */
        NO_IMAGE(0),
        /**
         * 一张图片
         */
        ONE_IMAGE(1),
        /**
         * 大于一张
         */
        MORE_IMAGE(2);
        final int type;

        TYPE(int type) {
            this.type = type;
        }
    }

    ViewArticleViewModel.Style mStyle;


    public ArticleRecAdapter(Context context, List<Article> data) {
        super(context, data);
    }

    public ArticleRecAdapter(Context context, List<Article> data, ViewArticleViewModel.Style style) {
        super(context, data);
        this.mStyle = style;
    }

    @Override
    protected int getItemCoreViewType(int truePos) {
        String imageUrls = getmData().get(truePos).getFirst_images();
        ArrayList<String> images = StringHelper.convertStringToList(getmData().get(truePos).getFirst_images());
        if (images == null) {
            if (imageUrls == null) {
                //可能是没有被正确的初始化,初始化完成如果没有图片会 imageUrls == ""
                images = HtmlHelper.getImgStr(getmData().get(truePos).getContainer(), 3);
                //设置初始化的图片
                getmData().get(truePos).setFirst_images(StringHelper.convertListToSrring(images));
            }
        }
        if (images == null || images.isEmpty()) {
            return TYPE.NO_IMAGE.ordinal();
        } else if (images.size() == 1) {
            return TYPE.ONE_IMAGE.ordinal();
        }
        return TYPE.MORE_IMAGE.ordinal();
    }

    @Override
    protected BindingHolder<ViewDataBinding> onCreateCoreViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        View convertView;

        if (viewType == TYPE.MORE_IMAGE.type) {
            convertView = mInflater.inflate(R.layout.lv_article_item_three_img, null);
            binding = LvArticleItemThreeImgBinding.bind(convertView);
        } else if (viewType == TYPE.ONE_IMAGE.type) {
            convertView = mInflater.inflate(R.layout.lv_article_item_one_img, null);
            binding = LvArticleItemOneImgBinding.bind(convertView);
        } else {
            convertView = mInflater.inflate(R.layout.lv_article_item_no_img, null);
            binding = LvArticleItemNoImgBinding.bind(convertView);
        }


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding = ScreenUtils.getResources().getDimensionPixelOffset(R.dimen.normal_padding);
        lp.setMargins(padding, padding, padding, 0);

        convertView.setLayoutParams(lp);
        convertView.setTag(binding);
        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }

    @Override
    protected void onBindCoreViewHolder(BindingHolder<ViewDataBinding> holder, int truePos) {
        Article article = mData.get(truePos);
        final int type = getItemCoreViewType(truePos);
        if (type == TYPE.MORE_IMAGE.type) {//三张图片
            LvArticleItemThreeImgBinding binding = ((LvArticleItemThreeImgBinding) holder.getBinding());
            binding.setArticle(article);
            if (mStyle != null)
                ((LvArticleItemThreeImgBinding) holder.getBinding()).setStyle(mStyle);
            binding.articleImages.setImages(StringHelper.convertStringToList(article.getFirst_images()));
        } else if (type == TYPE.ONE_IMAGE.type) {//一张图片
            LvArticleItemOneImgBinding binding = ((LvArticleItemOneImgBinding) holder.getBinding());
            binding.setArticle(article);
            if (mStyle != null)
                ((LvArticleItemOneImgBinding) holder.getBinding()).setStyle(mStyle);
            String imageUrl = StringHelper.convertStringToList(article.getFirst_images()).get(0);
            binding.atricleImage.setImageURI(Uri.parse(imageUrl));
        } else {//纯文字
            ((LvArticleItemNoImgBinding) holder.getBinding()).setArticle(article);
            if (mStyle != null)
                ((LvArticleItemNoImgBinding) holder.getBinding()).setStyle(mStyle);
        }
    }
}
