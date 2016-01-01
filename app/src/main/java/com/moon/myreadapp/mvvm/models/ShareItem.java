package com.moon.myreadapp.mvvm.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by moon on 16/1/1.
 */
public class ShareItem {

    private Builder mBuilder;

    private ShareItem(Builder builder) {
        mBuilder = builder;
    }

    public
    @DrawableRes
    int getIcon() {
        return mBuilder.mIconRes;
    }

    public CharSequence getContent() {
        return mBuilder.mContent;
    }

    public static class Builder {

        private Context mContext;
        protected
        @DrawableRes
        int mIconRes;
        protected CharSequence mContent;

        public Builder(Context context) {
            mContext = context;
        }


        public Builder icon(@DrawableRes int iconRes) {
            mIconRes = iconRes;
            return this;
        }

        public Builder content(CharSequence content) {
            this.mContent = content;
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            return content(mContext.getString(contentRes));
        }

        public ShareItem build() {
            return new ShareItem(this);
        }
    }

}
