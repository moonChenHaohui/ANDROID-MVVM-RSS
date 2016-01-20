package com.moon.myreadapp.mvvm.models.dao;

import com.moon.myreadapp.mvvm.viewmodels.DrawerViewModel;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FEED".
 */
public class Feed extends BmobObject {

    private Long id;
    /**
     * Not-null value.
     */
    private String title;
    private String url;
    private Integer status;
    private Integer use_count;
    private String description;
    private String feedtype;
    private String link;
    private String icon;
    private java.util.Date publishtime;
    private java.util.Date update_time;
    private String current_image;
    private String language;
    private String rights;
    private String uri;
    private String creator;
    private long user_id;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient FeedDao myDao;

    private User user;
    private Long user__resolvedKey;


    public Feed() {
    }

    public Feed(Long id) {
        this.id = id;
    }

    public Feed(Long id, String title, String url, Integer status, Integer use_count, String description, String feedtype, String link, String icon, java.util.Date publishtime, java.util.Date update_time, String current_image, String language, String rights, String uri, String creator, long user_id) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.status = status;
        this.use_count = use_count;
        this.description = description;
        this.feedtype = feedtype;
        this.link = link;
        this.icon = icon;
        this.publishtime = publishtime;
        this.update_time = update_time;
        this.current_image = current_image;
        this.language = language;
        this.rights = rights;
        this.uri = uri;
        this.creator = creator;
        this.user_id = user_id;
    }

    public void clearBmobData(){
        setCreatedAt(null);
        //setObjectId(null);
        setUpdatedAt(null);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFeedDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUse_count() {
        return use_count;
    }

    public void setUse_count(Integer use_count) {
        this.use_count = use_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeedtype() {
        return feedtype;
    }

    public void setFeedtype(String feedtype) {
        this.feedtype = feedtype;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public java.util.Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(java.util.Date publishtime) {
        this.publishtime = publishtime;
    }

    public java.util.Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(java.util.Date update_time) {
        this.update_time = update_time;
    }

    public String getCurrent_image() {
        return current_image;
    }

    public void setCurrent_image(String current_image) {
        this.current_image = current_image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public User getUser() {
        long __key = this.user_id;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new DaoException("To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            user_id = user.getId();
            user__resolvedKey = user_id;
        }
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", objid='" + getObjectId() + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}
