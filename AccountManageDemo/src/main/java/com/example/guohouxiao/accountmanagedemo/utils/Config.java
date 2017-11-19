package com.example.guohouxiao.accountmanagedemo.utils;

/**
 * Created by guohouxiao on 2017/11/17.
 * 数据库属性
 */

public class Config {

    public static final String DB_NAME = "user.db";//数据库名
    public static final int DB_VERSION = 1;//数据库版本号

    public static final String TABLE_NAME = "User";//表名
    public static final String[] USER_COLUMNS = new String[]{"Id", "Username", "Password", "Email", "Sex", "Hobby", "Birthday"};//列定义
    public static final String USER_ID = "Id";//主码，定义为自增长
    public static final String USERNAME = "Username";//用户名
    public static final String PASSWORD = "Password";//密码
    public static final String EMAIL = "Email";//邮箱
    public static final String SEX = "Sex";//性别
    public static final String HOBBY = "Hobby";//爱好
    public static final String BIRTHDAY = "Birthday";//生日
}
