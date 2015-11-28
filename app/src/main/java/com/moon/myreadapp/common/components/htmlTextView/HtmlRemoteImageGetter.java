package com.moon.myreadapp.common.components.htmlTextView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.moon.myreadapp.R;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.ScreenUtils;

import java.net.URI;

/**
 * Created by moon on 15/11/27.
 */
public class HtmlRemoteImageGetter implements Html.ImageGetter {
    TextView container;
    URI baseUri;


    public HtmlRemoteImageGetter(TextView t, String baseUrl) {
        this.container = t;
        if (baseUrl != null) {
            this.baseUri = URI.create(baseUrl);
        }
    }

    public Drawable getDrawable(String source) {
        final UrlDrawable urlDrawable = new UrlDrawable();

        urlDrawable.setDrawable(Globals.getApplication().getResources().getDrawable(R.drawable.bg_empty_image), ScreenUtils.dpToPx(70));
        /**
         * 使用volley进行图片加载
         */
        ImageRequest imageRequest = new ImageRequest(
                source,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Drawable drawable = new BitmapDrawable(response);
                        urlDrawable.setDrawable(drawable, container.getWidth());
                        container.invalidate();
                        container.setText(container.getText());
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(container.getContext()).add(imageRequest);

        return urlDrawable;
    }


    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;
        private int height = -1;

        private void setDrawable(Drawable nDrawable, int width) {
            if (drawable != null) {
                drawable = null;
            }
            if (width <= 0) {
                width = nDrawable.getIntrinsicWidth() * 3;
            }

            drawable = nDrawable;


            //等比例缩放
            Rect bounds = new Rect(0, 0, width, (int) (width * 3 / 4));
            int newwidth = bounds.width();
            int newheight = bounds.height();
            double factor = 1;
            double fx = (double) ScreenUtils.dpToPx(drawable.getIntrinsicWidth()) / (double) newwidth;
            double fy = (double) ScreenUtils.dpToPx(drawable.getIntrinsicHeight()) / (double) newheight;
            factor = fx < fy ? fx : fy;
            if (factor < 1) factor = 1;
            newwidth = (int) (ScreenUtils.dpToPx(drawable.getIntrinsicWidth()) / factor);
            newheight = (int) (ScreenUtils.dpToPx(drawable.getIntrinsicHeight()) / factor);

            drawable.setBounds(0, 0, newwidth, newheight);
            setBounds(0, 0, newwidth, newheight);

            height = newheight;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }


    }

}
