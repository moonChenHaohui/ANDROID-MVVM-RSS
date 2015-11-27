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
import com.moon.appframework.common.log.XLog;
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

//        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable, this);
//
//        asyncTask.execute(source);


        urlDrawable.setDrawable(Globals.getApplication().getResources().getDrawable(R.drawable.bg_empty_image), ScreenUtils.dpToPx(70));
        ImageRequest imageRequest = new ImageRequest(
                source,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Drawable drawable = new BitmapDrawable(response);
                        urlDrawable.setDrawable(drawable, container.getWidth());
                        container.setHeight((container.getHeight() + urlDrawable.getHeight()));
                        XLog.d("drawable + fix h:" + urlDrawable.getHeight());
                        //container.setEllipsize(null);
                        container.invalidate();

                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(container.getContext()).add(imageRequest);
        return urlDrawable;
    }

//    private static class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
//        private final WeakReference<UrlDrawable> drawableReference;
//        private final WeakReference<HtmlRemoteImageGetter> imageGetterReference;
//        private String source;
//
//        public ImageGetterAsyncTask(UrlDrawable d, HtmlRemoteImageGetter imageGetter) {
//            this.drawableReference = new WeakReference<>(d);
//            this.imageGetterReference = new WeakReference<>(imageGetter);
//
//        }
//
//        @Override
//        protected Drawable doInBackground(String... params) {
//            source = params[0];
//            return fetchDrawable(source);
//        }
//
//        @Override
//        protected void onPostExecute(Drawable result) {
//            if (result == null) {
//                Log.w(HtmlTextView.TAG, "Drawable result is null! (source: " + source + ")");
//                return;
//            }
//            final UrlDrawable urlDrawable = drawableReference.get();
//            if (urlDrawable == null) {
//                return;
//            }
//
//            //0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
//            // change the reference of the current drawable to the result from the HTTP call
//            urlDrawable.setDrawable(result);
//
//            final HtmlRemoteImageGetter imageGetter = imageGetterReference.get();
//            if (imageGetter == null) {
//                return;
//            }
//
//            imageGetter.container.setHeight((imageGetter.container.getHeight() + ScreenUtils.dpToPx(result
//                    .getIntrinsicHeight())));
//            //XLog.d("drawable + fix h:" + ScreenUtils.dpToPx(result.getIntrinsicHeight()));
//            imageGetter.container.setEllipsize(null);
//            // redraw the image by invalidating the container
//            imageGetter.container.invalidate();
//        }
//
//        /**
//         * Get the Drawable from URL
//         */
//        public Drawable fetchDrawable(String urlString) {
//            try {
//                InputStream is = fetch(urlString);
//                Drawable drawable = Drawable.createFromStream(is, "src");
//                return drawable;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//
//        private InputStream fetch(String urlString) throws IOException {
//            URL url;
//            final HtmlRemoteImageGetter imageGetter = imageGetterReference.get();
//            if (imageGetter == null) {
//                return null;
//            }
//            if (imageGetter.baseUri != null) {
//                url = imageGetter.baseUri.resolve(urlString).toURL();
//            } else {
//                url = URI.create(urlString).toURL();
//            }
//            XLog.d("drawable+ url:" + url);
//            return (InputStream) url.getContent();
//        }
//
//
//    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;
        private int height = -1;

        private void setDrawable(Drawable nDrawable, int width) {
            if (drawable != null){
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
            double fx = (double) drawable.getIntrinsicWidth() / (double) newwidth;
            double fy = (double) drawable.getIntrinsicHeight() / (double) newheight;
            factor = fx > fy ? fx : fy;
            //if (factor < 1) factor = 1;
            newwidth = (int) (drawable.getIntrinsicWidth() / factor);
            newheight = (int) (drawable.getIntrinsicHeight() / factor);

            drawable.setBounds(0, 0, newwidth, newheight);

            // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            setBounds(0, 0, newwidth, newheight);

            height = newheight;

            XLog.d("drawablesize:w:" + newwidth + ",h:" + newheight);
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
