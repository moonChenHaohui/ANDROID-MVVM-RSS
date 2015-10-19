package com.moon.myreadapp.read.homepage.business.bean;

import java.util.List;

/**
 * Created by moon on 15/10/4.
 */
public class Jocks {
    private String status;
    private String desc;
    private List<Jock> detail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Jock> getDetail() {
        return detail;
    }

    public void setDetail(List<Jock> detail) {
        this.detail = detail;
    }
}
