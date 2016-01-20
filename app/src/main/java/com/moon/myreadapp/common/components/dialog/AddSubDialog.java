package com.moon.myreadapp.common.components.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.databinding.FragmentAddSubBinding;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.HtmlHelper;
import com.moon.myreadapp.util.ViewUtils;
import com.rey.material.app.Dialog;

import java.util.ArrayList;

/**
 * Created by moon on 16/1/6.
 */
public class AddSubDialog extends Dialog {

    private FragmentAddSubBinding binding;
    private boolean isLoad;
    private boolean isSearch = true;
    private Feed feed;
    private ArrayList<Article> articles;

    public AddSubDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(R.string.dialog_sub_title);
        layoutParams(-1, -2);
        canceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_add_sub, null, false);
        setContentView(binding.getRoot());
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoad) {
                    if (isSearch) {
                        search();
                    } else {
                        sub();
                    }
                }
            }
        });
    }

    private void search() {

        String enter = binding.input.getText().toString();
        if (StringUtils.isEmpty(enter)) {
            binding.input.setError(BuiltConfig.getString(R.string.dialog_sub_search_empty));
        } else {
            binding.getRoot().post(new Runnable() {
                @Override
                public void run() {
                    showProgress(true);
                }
            });
            RssHelper.getMostRecentNews(binding.input.getText().toString(), new RssHelper.IRssListener() {
                @Override
                public void onSuccess(final SyndFeed syndFeed) {
                    binding.getRoot().post(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                            Feed feed = DBHelper.Util.feedConert(syndFeed, DBHelper.Query.getUserId());
                            feed.setUrl(binding.input.getText().toString());
                            ArrayList<Article> articles = DBHelper.Util.getArticles(syndFeed);
                            success(feed, articles);
                        }
                    });

                }

                @Override
                public void onError(final String msg) {
                    binding.getRoot().post(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                            wrong(BuiltConfig.getString(R.string.dialog_sub_search_error) + msg);
                        }
                    });
                }
            });
        }
    }

    private void wrong(final String note) {
        binding.input.setEnabled(true);
        ViewUtils.editViewFocus(binding.input, false);
        binding.input.setError(note);
        ViewUtils.editViewFocus(binding.input, true);
    }

    private void success(Feed feed, ArrayList<Article> articles) {
        this.feed = feed;
        this.articles = articles;
        isSearch = false;
        binding.login.setText(Globals.getApplication().getString(R.string.dialog_sub_action));
        binding.input.setEnabled(false);
        binding.input.setText(feed.getTitle());
        binding.input.setHint(Globals.getApplication().getString(R.string.dialog_subing_title));

    }

    private void sub() {
        //拿到转化好的feed 和文章列表
        //1.比对现有feed 中是否有相同的,a:有则更新,b:没有则插入.
        //2.比对articles,有则更新,没有则插入.
        //3.文章列表上传至服务器.
        if (feed == null) {
            showProgress(false);
            return;
        }
        binding.login.post(new Runnable() {
            @Override
            public void run() {
                binding.login.setText(Globals.getApplication().getString(R.string.dialog_sub_search_load));
                String icon = HtmlHelper.getIconUrlString(feed.getLink());
                feed.setIcon(icon);
                //取消bmob数据
                feed.setObjectId(null);
                feed.clearBmobData();
                long id = DBHelper.Insert.feed(feed);
                if (articles != null) {
                    for (int i = 0; i < articles.size(); i++) {
                        articles.get(i).setFeed_id(id);
                        DBHelper.Insert.article(articles.get(i));
                    }
                }
                XApplication.getInstance().bus.post(new UpdateFeedListEvent());
                ToastHelper.showToast(R.string.dialog_sub_success);
                dismiss();
            }
        });

    }

    private void showProgress(boolean show) {
        isLoad = show;
        isSearch = true;
        binding.login.setText(Globals.getApplication().getString(show ? R.string.dialog_sub_search_load : R.string.dialog_sub_search));
        binding.input.setEnabled(!show);
    }
}
