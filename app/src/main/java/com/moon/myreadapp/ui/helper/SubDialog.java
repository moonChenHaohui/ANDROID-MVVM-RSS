package com.moon.myreadapp.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.action.EventAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.common.event.UpdateArticleEvent;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.HtmlHelper;
import com.moon.myreadapp.util.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by moon on 15/10/22.
 * 添加订阅源的dialog
 * TODO 网络请求
 * TODO 请求数据返回的处理
 */
public class SubDialog extends MaterialDialog {


    private Context mContext;
    private MaterialEditText editText;

    private IconTextView load;

    /**
     * 可以带入文字
     *
     * @param context
     * @param url
     */
    public SubDialog(Context context, String url) {
        this(context);
        mContext = context;
        editText.setText(url);
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.editViewFocus(editText, true);
    }

    public SubDialog(Context context) {
        super(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_sub, null);
        setTitle(R.string.dialog_sub_title);
        setContentView(view);
        editText = (MaterialEditText) view.findViewById(R.id.edit);
        load = (IconTextView) view.findViewById(R.id.load);

        setPositiveButton(R.string.dialog_sub_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enter = editText.getText().toString();
                if (StringUtils.isEmpty(enter)) {
                    editText.setError(BuiltConfig.getString(R.string.dialog_sub_search_empty));
                } else {
                    load.setVisibility(View.VISIBLE);
                    editText.setEnabled(false);

                    RssHelper.getMostRecentNews(editText.getText().toString(), new RssHelper.IRssListener() {
                        @Override
                        public void onSuccess(final SyndFeed syndFeed) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    Feed feed = DBHelper.Util.feedConert(syndFeed, DBHelper.Query.getUserId());
                                    ArrayList<Article> articles = DBHelper.Util.getArticles(syndFeed);
                                    success(feed,articles);
                                }
                            });

                        }

                        @Override
                        public void onError(final String msg) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    wrong(BuiltConfig.getString(R.string.dialog_sub_search_error)+ msg);
                                }
                            });
                        }
                    });
                }
            }
        });
        setNegativeButton(R.string.dialog_sub_cacel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void wrong(final String note) {
        if (load.getVisibility() == View.VISIBLE) {
            load.setVisibility(View.GONE);
        }
        editText.setEnabled(true);
        ViewUtils.editViewFocus(editText, false);
        editText.setError(note);
        ViewUtils.editViewFocus(editText, true);
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
        //XDispatcher.from(mContext).dispatch(new EventAction(new UpdateArticleEvent()));
        dismiss();
        XLog.d("save successed ! the id :" + id);
        //XLog.d(feed1.toString());
        // 如果用户存在,则发送给服务端

    }

    @Override
    public void dismiss() {
        super.dismiss();
        mContext = null;
    }
}
