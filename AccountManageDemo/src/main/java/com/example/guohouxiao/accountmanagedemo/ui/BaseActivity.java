package com.example.guohouxiao.accountmanagedemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.guohouxiao.accountmanagedemo.database.UserDao;

/**
 * Created by guohouxiao on 2017/11/17.
 * Activity基类，初始化数据库
 */

public class BaseActivity extends AppCompatActivity {

    protected UserDao userDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDao = new UserDao(this);
        if (!userDao.isDataExist()) {
            userDao.initTable();
        }
    }
}
