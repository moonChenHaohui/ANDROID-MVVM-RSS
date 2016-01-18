package com.moon.myreadapp.mvvm.models;

/**
 * Created by moon on 16/1/18.
 * 同步状态
 */
public class SyncState {
    private boolean isSpin;
    private String notice;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean isSpin() {
        return isSpin;
    }

    public void setIsSpin(boolean isSpin) {
        this.isSpin = isSpin;
    }
}
