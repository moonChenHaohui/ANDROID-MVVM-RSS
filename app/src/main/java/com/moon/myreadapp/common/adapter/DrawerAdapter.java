package com.moon.myreadapp.common.adapter;

import android.databinding.DataBindingUtil;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.base.BaseListAdapter;
import com.moon.myreadapp.databinding.LeftDrawerListItemBinding;
import com.moon.myreadapp.mvvm.models.MenuItem;
import com.moon.myreadapp.mvvm.viewmodels.ViewArticleViewModel;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;

import java.util.List;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerAdapter extends BaseListAdapter<MenuItem> {

    public DrawerAdapter(List<MenuItem> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftDrawerListItemBinding binding;
        if (convertView == null){
            binding = DataBindingUtil.inflate(mInflater,R.layout.left_drawer_list_item,parent,false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        binding = (LeftDrawerListItemBinding)convertView.getTag();
        adapaterDataTips(getmData().get(position));
        binding.setItem(getmData().get(position));
        return convertView;
    }


    private static void adapaterDataTips (MenuItem item){
        final String title = item.getTitle();
        long count = -1;
        if (title.equals(BuiltConfig.getString(R.string.allunread))){
            count = DBHelper.Query.getArticlesCountByStyle(ViewArticleViewModel.Style.VIEW_UNREAD);
        } else if (title.equals(BuiltConfig.getString(R.string.myfavor))){
            count = DBHelper.Query.getArticlesCountByStyle(ViewArticleViewModel.Style.VIEW_FAVOR);
        } else if (title.equals(BuiltConfig.getString(R.string.readhistory))){
            count = DBHelper.Query.getArticlesCountByStyle(ViewArticleViewModel.Style.VIEW_READ_HISTORY);
        }
        if (count > 0) {
            item.setTips(Long.toString(count));
        }else {
            item.setTips(null);
        }
    }

}
