package com.example.guohouxiao.accountmanagedemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.guohouxiao.accountmanagedemo.R;
import com.example.guohouxiao.accountmanagedemo.utils.ShowToast;

/**
 * Created by guohouxiao on 2017/11/14.
 * 忘记密码
 */

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_username;
    private EditText et_email;
    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView();
    }

    private void initView() {

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String username = et_username.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email)) {
                    ShowToast.showShortToast(this, "邮件已发送，请查收并设定新密码！");
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
        }
    }
}
