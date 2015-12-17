package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.AddSubViewPagerAdapter;
import com.moon.myreadapp.common.adapter.SystemRecAdapter;
import com.moon.myreadapp.ui.AddFeedActivity;
import com.moon.myreadapp.ui.fragments.OPMLFragment;
import com.moon.myreadapp.ui.fragments.RecommendFragment;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/12/17.
 */
public class AddFeedViewModel extends BaseViewModel {

    private AddSubViewPagerAdapter adapter;
    private SystemRecAdapter systemRecAdapter;

    private String[] mTitleList;//页卡标题集合
    private List<Fragment> mFragments;//页卡视图集合

    private AddFeedActivity mView;

    public AddFeedViewModel(AddFeedActivity mView) {
        this.mView = mView;
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        //添加页卡标题
        mTitleList = mView.getResources().getStringArray(R.array.add_feed);
        mFragments = new ArrayList<>();

        mFragments.add(RecommendFragment.newInstance().createWithViewModel(this));//系统推荐
        mFragments.add(OPMLFragment.newInstance());//文件导入
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        adapter = new AddSubViewPagerAdapter(mView.getSupportFragmentManager(), mTitleList, mFragments);
        systemRecAdapter = new SystemRecAdapter(DBHelper.Query.getFeeds());
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void clear() {
        mView = null;
    }

    @Bindable
    public AddSubViewPagerAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public SystemRecAdapter getSystemRecAdapter() {
        return systemRecAdapter;
    }

    /**
     * 点击自定义输入 url 的dialog
     */
    public void onClickAddSub(View view) {
        DialogFractory.create(mView, DialogFractory.Type.AddSubscrible).show();
    }
}
