package com.moon.myreadapp.constants;

import com.moon.myreadapp.R;
import com.moon.myreadapp.util.Globals;

/**
 * Created by moon on 15/10/22.
 */
public class Constants {


    public static boolean DEBUG = true;

    public static String APP_URL =  "http://rssread.bmob.cn";

    /**
     * Bmob APP id
     */
    public static final String APP_ID = "1c56866927e32c7063d2179cc121a741";

    /**
     * 微信开发者id
     */
    public static String APP_WX_ID =  "null";

    public static final String DB_NAME         =   "eaasy-rss-db";

    public static final String FEED     =   "feed_object";

    public static final String FEED_ID     =   "feed_object_id";

    public static final String ARTICLE  =   "article_object";

    public static final String ARTICLE_ID   = "article_object_id";

    public static final String ARTICLE_POS   = "article_object_pos";

    public static final String ARTICLE_TITLE   = "article_object_title";

    public static final String ARTICLE_URL   = "article_object_url";

    public static final String IMAGES_LIST = "images_list";

    public static final String IMAGES_NOW_POSITION = "images_now_position";

    public static final String VIEW_ARTICLE_TYPE = "view_article_type";


    /**
     * 配置类
     */

    /**
     * app是否第一次进入
     */
    public static final String APP_IS_FIRST_USE = "app_is_first_use";

    /**
     * 文章字体大小
     */
    public static final String ARTICLE_FONT_SIZE = "article_font_size";

    /**
     * 频道内文章显示
     */
    public static final String FEED_SHOW_ALL = Globals.getApplication().getString(R.string.set_auto_show_unread_key);
    public static boolean showUnReadArticles = false;

    /**
     * 最小的文章内容大小,当开启智能打开原文时,小于这个值会直接打开原文链接
     */
    public static int MIN_CONTAINER_SIZE = 100;


    /**
     * 单次加载文章的数量
     */
    public static int SINGLE_LOAD_SIZE = 10;


}
