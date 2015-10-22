package com.moon.myreadapp.mvvm.models;

/**
 * Created by moon on 15/10/19.
 */
public class MenuItem {


    protected Builder mBUuilder;

    protected MenuItem(Builder builder) {
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

    public void setIcon(String icon) {
        mBUuilder.icon = icon;
    }

    public void setTitle(String title) {
        mBUuilder.title = title;
    }

    public void setUnReadIndicatorCount(int unReadIndicatorCount) {
        mBUuilder.unReadIndicatorCount = unReadIndicatorCount;
    }

    public void setTips(String tips) {
        mBUuilder.tips = tips;
    }

    public void setUrl(String url) {
        mBUuilder.url = url;
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


        public MenuItem build() {
            return new MenuItem(this);
        }
    }

}
