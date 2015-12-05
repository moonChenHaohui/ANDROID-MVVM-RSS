package com.moon.myreadapp.common.components.htmlTextView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.R;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.ui.ArticleActivity;
import com.moon.myreadapp.ui.ArticleWebActivity;
import com.moon.myreadapp.ui.ImageBrowserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 富文本
 * Created by moon on 15/11/29.
 */
public class RichText extends TextView {


    private OnImageClickListener onImageClickListener;//图片点击回调


    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置富文本
     *
     * @param text 富文本
     */
    public void setRichText(String text) {
        Spanned spanned = Html.fromHtml(text, new HtmlRemoteImageGetter(this, null), new HtmlTagHandler());
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }

        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final ArrayList<String> imageUrls = new ArrayList<>();

        for (int i = 0, size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onImageClickListener != null) {
                        onImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(spanned);
        //控制 link 跳转
        setMovementMethod(CustomLinkMovementMethod.getInstance(getContext()));
        //setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        void imageClicked(ArrayList<String> imageUrls, int position);
    }

    /**
     * hold text links
     */
    static class CustomLinkMovementMethod extends LinkMovementMethod {



        public static CustomLinkMovementMethod getInstance(Context c) {
            movementContext = c;
            if (sInstance == null)
                sInstance = new CustomLinkMovementMethod();
            return sInstance;
        }
        private static Context movementContext;
        private static CustomLinkMovementMethod sInstance;


        public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
                if (link.length != 0) {
                    String url = link[0].getURL();

                    //XLog.d("this is a url:" + url);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ARTICLE_TITLE, movementContext.getResources().getString(R.string.title_activity_article_web_link_title,url));
                    bundle.putString(Constants.ARTICLE_URL, url);
                    XDispatcher.from(movementContext).dispatch(new RouterAction(ArticleWebActivity.class, bundle, true));
                    return true;
                }
            }
            return super.onTouchEvent(widget, buffer, event);
        }
    }

}
