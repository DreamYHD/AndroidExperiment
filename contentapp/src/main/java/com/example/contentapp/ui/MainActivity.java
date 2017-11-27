package com.example.contentapp.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contentapp.R;
import com.example.contentapp.bean.User;
import com.example.contentapp.utils.Config;
import com.example.contentapp.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn_update;
    private Button btn_query;
    private Button btn_query_all;

    private EditText et_qry_selection;
    private EditText et_qry_content;

    private EditText et_mod_username;
    private EditText et_content;
    private EditText et_selection;

    private RecyclerView rv_user;
    private LinearLayoutManager mLayoutManager;
    private UserAdapter mAdapter;

    private List<User> list = new ArrayList<>();//原来数据库中的数据
    private List<User> search_list = new ArrayList<>();//查询到的数据
    private static final String TAG = "MainActivity";
    ContentResolver contentResolver;
    Uri uri = Uri.parse("content://com.example.guohouxiao.accountmanagedemo.contentProvider.MyProvider/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        btn_query_all = findViewById(R.id.btn_qry_all);
        btn_update = findViewById(R.id.btn_update);
        btn_query = findViewById(R.id.btn_query);
        rv_user = findViewById(R.id.rv_user);
        showAllUser();
        btn_update.setOnClickListener(view -> showUpdateDialog());
        btn_query.setOnClickListener( view -> showQueryDialog());
        btn_query_all.setOnClickListener( view -> showAllUser());
    }

    /**
     * 列表展示获取内容
     */
    private void showAllUser() {
        list.clear();
        getContent();
        mLayoutManager = new LinearLayoutManager(this);
        rv_user.setLayoutManager(mLayoutManager);
        mAdapter = new UserAdapter(this, list);
        rv_user.setAdapter(mAdapter);
    }

    /**
     * 数据库行元素转换为user
     * @param cursor 数据库行元素
     * @return 转换结果
     */
    private User parseUser(Cursor cursor) {
        User user = new User();
        user.id = (cursor.getInt(cursor.getColumnIndex(Config.USER_ID)));
        user.username = (cursor.getString(cursor.getColumnIndex(Config.USERNAME)));
        user.password = (cursor.getString(cursor.getColumnIndex(Config.PASSWORD)));
        user.email = (cursor.getString(cursor.getColumnIndex(Config.EMAIL)));
        user.sex = (cursor.getString(cursor.getColumnIndex(Config.SEX)));
        user.hobby = (cursor.getString(cursor.getColumnIndex(Config.HOBBY)));
        user.birthday = (cursor.getString(cursor.getColumnIndex(Config.BIRTHDAY)));
        return user;
    }

    /**
     * 从远程端获取数据库
     */
    public void getContent() {
        contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                uri,
                null,
                "query_where",
                null,
                null);
        Toast.makeText(this, "远端 ContentProvider 返回的 Cursor 为：" + cursor,
                Toast.LENGTH_LONG).show();
        while (cursor.moveToNext()){
            list.add(parseUser(cursor));
        }
    }

    private void showUpdateDialog() {
        contentResolver = getContentResolver();
        View dialogUpdate = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        et_mod_username = dialogUpdate.findViewById(R.id.et_mod_username);
        et_selection = dialogUpdate.findViewById(R.id.et_selection);
        et_content = dialogUpdate.findViewById(R.id.et_content);
         new AlertDialog.Builder(this)
                .setView(dialogUpdate)
                .setTitle("修改数据")
                .setPositiveButton("确定", (dialogInterface, i) -> doChange())
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void showQueryDialog() {
        View dialogQuery = LayoutInflater.from(this).inflate(R.layout.dialog_query, null);
        et_qry_selection = dialogQuery.findViewById(R.id.et_qry_selection);
        et_qry_content = dialogQuery.findViewById(R.id.et_qry_content);
        new AlertDialog.Builder(this)
                .setView(dialogQuery)
                .setTitle("查询数据")
                .setPositiveButton("确定", (dialogInterface, i) -> doSearch())
                .setNegativeButton("取消", null)
                .create().show();
    }
    /**
     * 根据属性修改
     */
    private void doChange() {
        String mod_username = et_mod_username.getText().toString().trim();
        String selection = et_selection.getText().toString().trim();
        String content = et_content.getText().toString().trim();
        ContentValues cv = new ContentValues();
        switch (selection) {
            case "邮箱":
                cv.put(Config.EMAIL, content);
                break;
            case "性别":
                cv.put(Config.SEX, content);
                break;
            case "爱好":
                cv.put(Config.HOBBY, content);
                break;
            case "生日":
                cv.put(Config.BIRTHDAY, content);
                break;
        }
        if (!TextUtils.isEmpty(mod_username) && !TextUtils.isEmpty(selection) && !TextUtils.isEmpty(content)) {
            if (contentResolver.update(uri, cv, mod_username, null)==0) {
                ShowToast.showShortToast(MainActivity.this, "修改成功！");
                showAllUser();
            } else {
                ShowToast.showShortToast(MainActivity.this, "修改失败！");
            }
        } else {
            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
        }
    }

    /**
     * 根据属性查询
     */
    private void doSearch() {
        String qry_selection = et_qry_selection.getText().toString().trim();
        String qry_content = et_qry_content.getText().toString().trim();
        if (!TextUtils.isEmpty(qry_selection) && !TextUtils.isEmpty(qry_content)) {
            search_list.clear();
            switch (qry_selection) {
                case "姓名":
                    for (User user:
                            list ) {
                        if (user.getUsername().equals(qry_content)){
                            search_list.add(user);
                        }
                    }
                    break;
                case "邮箱":
                    for (User user:
                            list ) {
                        if (user.getEmail().equals(qry_content)) {
                            search_list.add(user);
                        }
                    }
                    break;
                case "性别":
                    for (User user:
                            list ) {
                        if (user.getSex().equals(qry_content)) {
                            search_list.add(user);
                        }
                    }
                    break;
                case "爱好":
                    for (User user:
                            list ) {
                        if (user.getHobby().equals(qry_content)) {
                            search_list.add(user);
                        }
                    }
                    break;
                case "生日":
                    for (User user:
                            list ) {
                        if (user.getBirthday().equals(qry_content)) {
                            search_list.add(user);
                        }
                    }
                    break;
            }
            if (search_list.isEmpty()){
                ShowToast.showShortToast(MainActivity.this, "没有当前数据，查询失败");
            }else{
                ShowToast.showShortToast(MainActivity.this, "查询成功！");
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_user.setLayoutManager(mLayoutManager);
                mAdapter = new UserAdapter(getApplicationContext(), search_list);
                rv_user.setAdapter(mAdapter);
            }
        } else {
            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
        }

    }
}
