package com.moon.myreadapp.mvvm.models;

/**
 * Created by moon on 15/10/19.
 */
public class User {

    private int uid;
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
