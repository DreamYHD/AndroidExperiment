package com.example.guohouxiao.accountmanagedemo.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.guohouxiao.accountmanagedemo.R;
import com.example.guohouxiao.accountmanagedemo.utils.Config;
import com.example.guohouxiao.accountmanagedemo.utils.ShowToast;

import java.util.Calendar;

/**
 * Created by guohouxiao on 2017/11/14.
 * 注册
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_username;
    private EditText et_password;
    private EditText et_email;
    private EditText et_sex;
    private EditText et_hobby;
    private EditText et_birthday;

    private Button btn_submit;
    private Button btn_reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_sex = findViewById(R.id.et_sex);
        et_hobby = findViewById(R.id.et_hobby);
        et_birthday = findViewById(R.id.et_birthday);
        btn_submit = findViewById(R.id.btn_submit);
        btn_reset = findViewById(R.id.btn_reset);

        et_sex.setOnClickListener(this);
        et_hobby.setOnClickListener(this);
        et_birthday.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_sex:
                ShowSexDialog();
                break;
            case R.id.et_hobby:
                ShowHobbyDialog();
                break;
            case R.id.et_birthday:
                ShowDateDialog();
                break;
            case R.id.btn_submit:
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String hobby = et_hobby.getText().toString().trim();
                String birthday = et_birthday.getText().toString().trim();
                if (!TextUtils.isEmpty(username)
                        && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(sex)
                        && !TextUtils.isEmpty(hobby)
                        && !TextUtils.isEmpty(birthday)) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Config.USERNAME, username);
                    contentValues.put(Config.PASSWORD, password);
                    contentValues.put(Config.EMAIL, email);
                    contentValues.put(Config.SEX, sex);
                    contentValues.put(Config.HOBBY, hobby);
                    contentValues.put(Config.BIRTHDAY, birthday);
                    if (userDao.insertData(contentValues)) {
                        ShowToast.showShortToast(this, "注册成功！");
                    } else {
                        ShowToast.showShortToast(this, "注册失败！");
                    }
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.btn_reset:
                et_username.setText("");
                et_password.setText("");
                et_email.setText("");
                et_sex.setText("");
                et_hobby.setText("");
                et_birthday.setText("");
                break;
        }
    }

    private void ShowSexDialog() {
        final String[] sex = {"男", "女"};
        @SuppressLint("ResourceType")
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, 3);
        dialog.setTitle("选择你的性别");
        dialog.setSingleChoiceItems(sex, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                et_sex.setText(sex[i]);
            }
        });
        dialog.create().show();
    }

    private void ShowHobbyDialog() {
        final String[] hobby = {"学习", "听歌", "看书"};
        final boolean[] check = {false, false, false};
        @SuppressLint("ResourceType")
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, 3);
        dialog.setTitle("选择你的爱好");
        dialog.setMultiChoiceItems(hobby, check, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                check[i] = b;
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < hobby.length; j++) {
                    if (check[j]) {
                        str.append(hobby[j]);
                    }
                }
                et_hobby.setText(str);
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.create().show();
    }

    private void ShowDateDialog() {
        Calendar mDate = Calendar.getInstance();
        int year = mDate.get(Calendar.YEAR);
        int month = mDate.get(Calendar.MONTH);
        int day = mDate.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("ResourceType")
        DatePickerDialog dialog = new DatePickerDialog(this, 3, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                et_birthday.setText(DateFormat.format("yyyy-MM-dd", calendar));
            }
        }, year, month, day);
        dialog.show();
    }
}
