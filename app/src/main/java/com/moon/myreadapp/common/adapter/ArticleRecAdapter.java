package com.moon.myreadapp.common.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseRecyclerAdapter;
import com.moon.myreadapp.databinding.LvArticleItemBinding;
import com.moon.myreadapp.mvvm.models.dao.Article;

import java.util.List;

/**
 * Created by moon on 15/11/14.
 */
public class ArticleRecAdapter extends BaseRecyclerAdapter<Article,LvArticleItemBinding> {

    public ArticleRecAdapter(List<Article> data) {
        super(data);
    }

    @Override
    protected int getItemCoreViewType(int truePos) {
        return 1;
    }

    @Override
    protected BindingHolder<LvArticleItemBinding> onCreateCoreViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.lv_article_item, null);
        LvArticleItemBinding binding = LvArticleItemBinding.bind(convertView);
        convertView.setTag(binding);
        BindingHolder mHolder = new BindingHolder(convertView);
        return mHolder;
    }
    @Override
    protected void onBindCoreViewHolder(BindingHolder<LvArticleItemBinding> holder, int truePos) {
        Article article = mData.get(truePos);
        holder.getBinding().setArticle(article);
    }
}
