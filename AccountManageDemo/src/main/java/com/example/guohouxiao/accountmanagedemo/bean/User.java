package com.example.guohouxiao.accountmanagedemo.bean;

/**
 * Created by guohouxiao on 2017/11/16.
 * 用户实体类
 */

public class User {

    public int id;
    public String username;
    public String password;
    public String email;
    public String sex;
    public String hobby;
    public String birthday;

    public User() {
    }

    public User(int id, String username, String password, String email, String sex, String hobby, String birthday) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.hobby = hobby;
        this.birthday = birthday;
    }
}
