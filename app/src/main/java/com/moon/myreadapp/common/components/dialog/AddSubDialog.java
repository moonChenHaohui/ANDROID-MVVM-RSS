package com.moon.myreadapp.common.components.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.components.toast.SimpleToastHelper;
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
public class AddSubDialog extends Dialog{

    private FragmentAddSubBinding binding;
    private boolean isLoad;

    public AddSubDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dialog_sub_title);
        layoutParams(-1, -2);
        canceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_add_sub,null,false);
        setContentView(binding.getRoot());
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoad) {
                    search();
                }
            }
        });
    }

    private void search (){

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

    private void success(Feed feed,ArrayList<Article> articles) {
        //拿到转化好的feed 和文章列表
        //1.比对现有feed 中是否有相同的,a:有则更新,b:没有则插入.
        //2.比对articles,有则更新,没有则插入.
        //3.文章列表上传至服务器.
        String icon = HtmlHelper.getIconUrlString(feed.getLink());
        feed.setIcon(icon);
        long id = DBHelper.Insert.feed(feed);
        for(int i = 0 ;i < articles.size();i++){
            articles.get(i).setFeed_id(id);
            DBHelper.Insert.article(articles.get(i));
        }
        XApplication.getInstance().bus.post(new UpdateFeedListEvent());
        SimpleToastHelper.showToast(R.string.dialog_sub_success);
        dismiss();
        XLog.d("save successed ! the id :" + id);
        //XLog.d(feed1.toString());
        // 如果用户存在,则发送给服务端

    }
    private void showProgress(boolean show){
        isLoad = show;
        binding.login.setText(Globals.getApplication().getString(show ? R.string.dialog_sub_search_load:R.string.dialog_sub_search));
        binding.input.setEnabled(!show);
    }
}
