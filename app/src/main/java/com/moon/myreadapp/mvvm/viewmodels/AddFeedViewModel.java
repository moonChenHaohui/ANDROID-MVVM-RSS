package com.moon.myreadapp.mvvm.viewmodels;

import android.databinding.Bindable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.AddSubViewPagerAdapter;
import com.moon.myreadapp.common.adapter.SystemRecAdapter;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.ui.AddFeedActivity;
import com.moon.myreadapp.ui.fragments.OPMLFragment;
import com.moon.myreadapp.ui.fragments.RecommendFragment;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

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

        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        adapter = new AddSubViewPagerAdapter(mView.getSupportFragmentManager(), mTitleList, mFragments);
        systemRecAdapter = new SystemRecAdapter(mView,null);

        mFragments.add(RecommendFragment.newInstance().createWithViewModel(this));//系统推荐
        mFragments.add(OPMLFragment.newInstance());//文件导入
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
        DialogFractory.createDialog(mView, DialogFractory.Type.AddSubscrible).show();
        // DialogFractory.create(mView, DialogFractory.Type.AddSubscrible).show();
    }


    /**
     * 加载服务端数据
     *
     * @param emptyView
     */
    public void loadSystemData(final TextView emptyView) {
        if (emptyView == null){
            return;
        }
        emptyView.setText(emptyView.getResources().getString(R.string.sub_notice_loading));
        emptyView.setEnabled(false);
        emptyView.setVisibility(View.VISIBLE);
        BmobQuery<Feed> query = new BmobQuery<Feed>();
        query.findObjects(mView, new FindListener<Feed>() {
            @Override
            public void onSuccess(List<Feed> object) {
                systemRecAdapter.setmData(object);
                if (object.size() <= 0) {
                    emptyView.setText(emptyView.getResources().getString(R.string.sub_notice_empty_data));
                }
                emptyView.setEnabled(true);
                emptyView.setVisibility(object.size() <= 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onError(int code, String msg) {
                emptyView.setText(emptyView.getResources().getString(R.string.sub_notice_system_error));
                emptyView.setEnabled(true);
            }
        });
    }
}
