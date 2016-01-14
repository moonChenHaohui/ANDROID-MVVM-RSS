package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.dialog.TextFont;
import com.moon.myreadapp.common.components.htmlTextView.RichText;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityArticleBinding;
import com.moon.myreadapp.mvvm.viewmodels.ArticleViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.PreferenceUtils;

import java.util.ArrayList;

import de.halfbit.tinybus.Subscribe;

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
        articleViewModel = new ArticleViewModel(this, getIntent().getExtras().getLong(Constants.ARTICLE_ID, -1),getIntent().getExtras().getInt(Constants.ARTICLE_POS, -1));
        if (articleViewModel.getArticle() == null){
            setContentView(new View(this));
            DialogFractory.createDialog(this, DialogFractory.Type.EmptyView).
                    title(R.string.empty_article).
                    positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
            return;
        }
        binding = DataBindingUtil.setContentView(this, getLayoutView());
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
        if (articleViewModel.getArticle()!= null) {
            //富文本显示
            binding.articleBody.feedContent.setRichText(articleViewModel.getArticle().getContainer());
        }

        //设置文本大小
        binding.articleBody.feedContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, PreferenceUtils.getInstance(this).getIntParam(Constants.ARTICLE_FONT_SIZE, (int)Globals.getApplication().getResources().getDimension(TextFont.H3.size)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_read_all) {
            XDispatcher.from(this).dispatch(new RouterAction(ArticleWebActivity.class, true));
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onUpdateEvent(UpdateArticleEvent event) {
        binding.articleBody.feedContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,event.getFontSize());
    }

    @Override
    protected void onDestroy() {
        if (articleViewModel != null){
            articleViewModel.clear();
            articleViewModel = null;
        }
        super.onDestroy();
    }
}
