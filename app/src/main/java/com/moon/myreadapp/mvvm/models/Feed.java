package com.moon.myreadapp.mvvm.models;

/**
 * Created by moon on 15/10/23.
 */
public class Feed {

    /**
     * 标题
     */
    private String title;

    /**
     *  作者
     */
    private String author;

    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 内容
     */
    private String content;

    /**
     * 内容中的第一张图片
     */
    private String contentFirstImage;

    /**
     * 所属channel id
     */
    private int channelId;

    public Feed (String time){
        super();
        this.publishTime = time;
    }
    public Feed (){
        this.title = "测试标题";
        this.author = "moon";
        this.publishTime = "1";
        this.content = "今天进行feedlist的开发";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentFirstImage() {
        return contentFirstImage;
    }

    public void setContentFirstImage(String contentFirstImage) {
        this.contentFirstImage = contentFirstImage;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
