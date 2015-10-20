package com.moon.myreadapp.mvvm.models;

import java.io.Serializable;

/**
 * Created by moon on 15/10/19.
 */
public class BaseMenuItem implements Serializable {

    /**
     * 图标
     */
    private String icon;
    /**
     * 名字
     */
    private String title = "sasdsad";
    /**
     * 未读个数
     */
    private int unReadIndicatorCount;

    /**
     * 附属信息
     */
    private String tips;

    /**
     * 跳转url
     */
    private String url;

    private Builder mBUuilder;

    private BaseMenuItem(Builder builder) {
        mBUuilder = builder;
    }

    public String getUrl() {
        return mBUuilder.url;
    }

    public String getIcon() {
        return mBUuilder.icon;
    }

    public String getTitle() {
        return mBUuilder.title;
    }

    public String getTips() {
        return mBUuilder.tips;
    }


    public static class Builder {
        /**
         * 图标
         */
        private String icon;
        /**
         * 名字
         */
        private String title;
        /**
         * 未读个数
         */
        private int unReadIndicatorCount;

        /**
         * 附属信息
         */
        private String tips;

        /**
         * 跳转url
         */
        private String url;

        public Builder() {
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder unReadIndicatorCount(int unReadIndicatorCount) {
            this.unReadIndicatorCount = unReadIndicatorCount;
            return this;
        }

        public Builder tips(String tips) {
            this.tips = tips;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public BaseMenuItem build() {
            return new BaseMenuItem(this);
        }
    }

}
