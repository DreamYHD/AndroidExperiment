package com.example.guohouxiao.accountmanagedemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guohouxiao.accountmanagedemo.R;
import com.example.guohouxiao.accountmanagedemo.bean.User;
import com.example.guohouxiao.accountmanagedemo.utils.Config;
import com.example.guohouxiao.accountmanagedemo.utils.ShareUtils;
import com.example.guohouxiao.accountmanagedemo.utils.ShowToast;
import com.example.guohouxiao.accountmanagedemo.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/11/14.
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText et_username;
    private EditText et_password;
    private TextView tv_forget;
    private Button btn_login;
    private Button btn_register;
    private CheckBox mCheckBox;

    private CustomDialog mDialog;

    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        CheckIsSaved();
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        tv_forget = findViewById(R.id.tv_forget);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        mCheckBox = findViewById(R.id.mCheckBox);

        //初始化dialog
        mDialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);

        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void CheckIsSaved() {
        String username = ShareUtils.getString(this, "username", null);
        String password = ShareUtils.getString(this, "password", null);
        if (username != null && password != null) {
            et_username.setText(username);
            et_password.setText(password);
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.btn_login:
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    if (userDao.getSomeData(Config.USERNAME, username) != null) {
                        userList.addAll(userDao.getSomeData(Config.USERNAME, username));
                        if (password.equals(userList.get(0).password)) {
                            if (mCheckBox.isChecked()) {
                                ShareUtils.putString(this, "username", username);
                                ShareUtils.putString(this, "password", password);
                            } else {
                                ShareUtils.deleShare(this, "username");
                                ShareUtils.deleShare(this, "password");
                            }
                            showDialogFor1S("");
                        } else {
                            showDialogFor1S("密码错误！");
                        }
                    } else {
                        showDialogFor1S("不存在当前用户名！");
                    }
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    //将弹出框显示时间控制在1秒
    private void showDialogFor1S(String toast) {
        mDialog.show();
        final String string = toast;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                if (string.equals("")) {
                    LoginActivity.this.finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    ShowToast.showShortToast(LoginActivity.this, string);
                }
            }
        }, 1000);
    }
}
