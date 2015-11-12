package com.moon.myreadapp.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.rss.RssHelper;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.ScreenUtils;
import com.moon.myreadapp.util.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by moon on 15/10/22.
 * 添加订阅源的dialog
 * TODO 网络请求
 * TODO 请求数据返回的处理
 */
public class SubDialog extends MaterialDialog {


    private MaterialEditText editText;

    private IconTextView load;

    /**
     * 可以带入文字
     * @param context
     * @param url
     */
    public SubDialog(Context context,String url) {
        this(context);
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
        load = (IconTextView)view.findViewById(R.id.load);

        setPositiveButton(R.string.dialog_sub_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(editText.getText())) {
                    editText.setError(BuiltConfig.getString(R.string.dialog_sub_search_empty));
                } else {
                    load.setVisibility(View.VISIBLE);
                    editText.setEnabled(false);
                    RssHelper.getMostRecentNews(editText.getText().toString(), new RssHelper.IRssListener() {
                        @Override
                        public void onSuccess(SyndFeed feed) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    success();
                                }
                            });

                        }

                        @Override
                        public void onError(String msg) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    wrong();
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


    private void wrong (){
        if (load.getVisibility() == View.VISIBLE) {
            load.setVisibility(View.GONE);
        }
        editText.setEnabled(true);
        ViewUtils.editViewFocus(editText, false);
        editText.setError(BuiltConfig.getString(R.string.dialog_sub_search_error));
        ViewUtils.editViewFocus(editText, true);
    }

    private void success (){

    }
}
