package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.dialog.FeedSetDialog;
import com.moon.myreadapp.common.components.pulltorefresh.PullToRefreshBase;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.common.event.UpdateUIEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityFeedBinding;
import com.moon.myreadapp.databinding.ActivityViewArticleBinding;
import com.moon.myreadapp.mvvm.viewmodels.FeedViewModel;
import com.moon.myreadapp.mvvm.viewmodels.ViewArticleViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.util.Conver;
import com.moon.myreadapp.util.PreferenceUtils;
import com.moon.myreadapp.util.ThemeUtils;

import java.util.Date;

import de.halfbit.tinybus.Subscribe;


/**
 *
 * 查看文章页面,可以查看 阅读历史\收藏文件\网页等..
 *
 */
public class ViewArticleActivity extends BaseActivity {


    @Override
    protected int getLayoutView() {
        return R.layout.activity_view_article;
    }

    private Toolbar toolbar;

    private ActivityViewArticleBinding binding;

    private ViewArticleViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        initToolBar(toolbar);
        initBusiness();
    }

    private void initBusiness (){

        binding.articleList.setAdapter(viewModel.getmAdapter());
        binding.articleList.setPullLoadEnabled(false);
        binding.articleList.setScrollLoadEnabled(true);
        binding.articleList.getHeaderLoadingLayout().setLastUpdatedLabel(Conver.ConverToString(new Date(), "HH:mm"));
        binding.articleList.getRefreshableView().addOnItemTouchListener(viewModel.getArticleClickListener());
        binding.articleList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //下拉刷新
                viewModel.refresh(binding.articleList);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //上拉加载
                binding.articleList.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean hasMoreData = viewModel.loadMore();
                        binding.articleList.onPullUpRefreshComplete();
                        binding.articleList.setHasMoreData(hasMoreData);
                    }
                });
            }
        });

    }


    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());
        viewModel = new ViewArticleViewModel(this, ViewArticleViewModel.Style.find(getIntent().getExtras().getInt(Constants.VIEW_ARTICLE_TYPE,-1)));
        binding.setViewmodel(viewModel);
    }


    @Subscribe
    public void onUpdateEvent(UpdateFeedEvent event) {
        if (event.getType() == UpdateFeedEvent.TYPE.STATUS){
            //更新单个article 状态
            if (event.getUpdatePosition() >= 0) {
                viewModel.updateArticleByPosition(event.getUpdatePosition(),event.getArticle());
            }
        }

    }

    @Override
    protected void onDestroy() {
        viewModel.clear();
        viewModel = null;
        super.onDestroy();
    }

    public void btnOnClick(View v) {
        viewModel.btnOnClick(v);
    }

}
