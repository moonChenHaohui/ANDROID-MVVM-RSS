package com.moon.myreadapp.util;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import com.moon.appframework.common.util.StringUtils;
import com.moon.myreadapp.R;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.viewmodels.ViewArticleViewModel;

import java.util.ArrayList;

/**
 * Created by moon on 15/12/6.
 */
public class StringHelper {

    public static String SPLIT = ",";

    public static ArrayList<String> convertStringToList(String images){
        if (StringUtils.isEmpty(images)){
            return null;
        }
        ArrayList<String> re = new ArrayList<String>();
        String[] result = images.split(SPLIT);
        for (String s : result){
            re.add(s);
        }
        result = null;
        return re;
    }

    public static String convertListToSrring(ArrayList<String> strings){
        if (strings == null || strings.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        int len = strings.size();
        for (int i = 0;i < len;i++){
            sb.append(strings.get(i));
            if (i < len - 1){
                sb.append(SPLIT);
            }
        }
        return sb.toString();
    }

    /**
     * 根据状态生成对应的html string
     * @param statusInt
     * @param title
     * @return
     */
    public static Spanned converTitleByStatus(String title, int statusInt){
        Article.Status status = Article.Status.find(statusInt);
        StringBuilder sb = new StringBuilder("");
        switch ((status)){
            case FAVOR:
                //收藏
                sb.append(Globals.getApplication().getResources().getString(R.string.font_favor));
                break;
            case DELETE:
                //已删除
                sb.append(Globals.getApplication().getResources().getString(R.string.font_delete));
                break;
            case NORMAL:
                //普通
                break;
            default:
                break;
        }
        return Html.fromHtml(sb.append(title).toString());
    }

    public static Spanned converInfoByStyle(Article article, ViewArticleViewModel.Style style,Feed feed){
        StringBuilder sb = new StringBuilder("");
        if (style != null) {
            switch ((style)) {
                case VIEW_FAVOR:
                    if (feed !=null && feed.getTitle() != null){
                        sb.append(Globals.getApplication().getResources().getString(R.string.font_feed_title,feed.getTitle()));
                    }
                    break;
                case VIEW_READ_HISTORY:
                    if (feed !=null && feed.getTitle() != null){
                        sb.append(Globals.getApplication().getResources().getString(R.string.font_feed_last_read_time,feed.getTitle(),article.getUse_count()));
                    } else {
                        sb.append(Globals.getApplication().getResources().getString(R.string.font_feed_last_read_time_no_feed,article.getUse_count()));
                    }
                    break;
                default:
                    break;
            }
        }
        return Html.fromHtml(sb.append(article.getCreator()).toString());
    }

}
