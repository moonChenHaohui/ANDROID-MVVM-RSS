package com.moon.myreadapp.ui;

import android.animation.Animator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.htmlTextView.RichText;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityArticleBinding;
import com.moon.myreadapp.mvvm.viewmodels.ArticleViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

import java.util.ArrayList;

public class ArticleActivity extends BaseActivity {


    private Toolbar toolbar;
    private ActivityArticleBinding binding;

    private ArticleViewModel articleViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar);
    }

    @Override
    protected Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_article;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());

        articleViewModel = new ArticleViewModel(this, getIntent().getExtras().getLong(Constants.ARTICLE_ID, -1),getIntent().getExtras().getInt(Constants.ARTICLE_POS, -1));
        binding.setArticleViewModel(articleViewModel);


        //图片浏览
        binding.articleBody.feedContent.setOnImageClickListener(new RichText.OnImageClickListener() {
            @Override
            public void imageClicked(ArrayList<String> imageUrls, int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constants.IMAGES_LIST, imageUrls);
                bundle.putInt(Constants.IMAGES_NOW_POSITION, position);
                XDispatcher.from(ArticleActivity.this).dispatch(new RouterAction(ImageBrowserActivity.class, bundle, true));

            }
        });
        //富文本显示
        binding.articleBody.feedContent.setRichText(articleViewModel.getArticle().getContainer());
        //ObjectAnimator.ofFloat(binding.buttonBar, "translationX",300).setDuration(3000).start();
        // ViewHelper.setScaleX(binding.buttonBar,200);

//        Animator animator = ViewAnimationUtils.createCircularReveal(
//                binding.articleBottomBar.buttonBar,
//                binding.articleBottomBar.buttonBar.getWidth()/2,
//                binding.articleBottomBar.buttonBar.getHeight()/2,
//                binding.articleBottomBar.buttonBar.getWidth(),0);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(2000);
//        animator.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.content) {
            finish();
        } else if (id == R.id.action_read_all) {
            XDispatcher.from(this).dispatch(new RouterAction(ArticleWebActivity.class, true));
        }
        return super.onOptionsItemSelected(item);
    }
}
