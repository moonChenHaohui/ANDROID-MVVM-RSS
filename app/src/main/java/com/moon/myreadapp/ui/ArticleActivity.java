package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.ActivityArticleBinding;
import com.moon.myreadapp.mvvm.viewmodels.ArticleViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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

        articleViewModel = new ArticleViewModel(this, getIntent().getExtras().getLong(Constants.ARTICLE_ID, -1));
        binding.setArticleViewModel(articleViewModel);
        Spanned spanned = Html.fromHtml(articleViewModel.getArticle().getContainer(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                new LoadImage().execute(source, d);
                return d;

            }

            class LoadImage extends AsyncTask<Object, Void, Bitmap> {

                private LevelListDrawable mDrawable;

                @Override
                protected Bitmap doInBackground(Object... params) {
                    String source = (String) params[0];
                    mDrawable = (LevelListDrawable) params[1];
                    //Log.d(TAG, "doInBackground " + source);
                    try {
                        InputStream is = new URL(source).openStream();
                        return BitmapFactory.decodeStream(is);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    //Log.d(TAG, "onPostExecute drawable " + mDrawable);
                    //Log.d(TAG, "onPostExecute bitmap " + bitmap);
                    if (bitmap != null) {
                        BitmapDrawable d = new BitmapDrawable(bitmap);
                        mDrawable.addLevel(1, 1, d);
                        mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        mDrawable.setLevel(1);
                        // i don't know yet a better way to refresh TextView
                        // mTv.invalidate() doesn't work as expected
                        CharSequence t = binding.feedBody.feedContent.getText();
                        binding.feedBody.feedContent.setText(t);
                    }
                }
            }
        }, null);
        XLog.d("spanned:" + spanned.toString());
        binding.feedBody.feedContent.setText(spanned);
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
