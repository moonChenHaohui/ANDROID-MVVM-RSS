package com.moon.myreadapp.mvvm.models.dao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FEED.
 */
public class Feed {

    private Long id;
    /** Not-null value. */
    private String title;
    private Integer use_count;
    private String description;
    private String feedtype;
    private String link;
    private java.util.Date publishtime;
    private String language;
    private String rights;
    private String uri;
    private String creator;
    private long user_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FeedDao myDao;

    private User user;
    private Long user__resolvedKey;


    public Feed() {
    }

    public Feed(Long id) {
        this.id = id;
    }

    public Feed(Long id, String title, Integer use_count, String description, String feedtype, String link, java.util.Date publishtime, String language, String rights, String uri, String creator, long user_id) {
        this.id = id;
        this.title = title;
        this.use_count = use_count;
        this.description = description;
        this.feedtype = feedtype;
        this.link = link;
        this.publishtime = publishtime;
        this.language = language;
        this.rights = rights;
        this.uri = uri;
        this.creator = creator;
        this.user_id = user_id;
    }

    /** called by internal mechanisms, do not call yourself. */
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

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
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

    public java.util.Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(java.util.Date publishtime) {
        this.publishtime = publishtime;
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

    /** To-one relationship, resolved on first access. */
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

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
