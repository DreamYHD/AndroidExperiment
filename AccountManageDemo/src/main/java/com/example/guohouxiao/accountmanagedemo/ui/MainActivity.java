package com.example.guohouxiao.accountmanagedemo.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.guohouxiao.accountmanagedemo.R;
import com.example.guohouxiao.accountmanagedemo.adapter.UserAdapter;
import com.example.guohouxiao.accountmanagedemo.bean.User;
import com.example.guohouxiao.accountmanagedemo.utils.Config;
import com.example.guohouxiao.accountmanagedemo.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/11/14.
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_insert;
    private Button btn_delete;
    private Button btn_update;
    private Button btn_query;

    private RecyclerView rv_user;
    private LinearLayoutManager mLayoutManager;
    private UserAdapter mAdapter;
    private List<User> mList = new ArrayList<>();

    private AlertDialog dialog_insert;
    private EditText et_add_username;
    private EditText et_add_password;

    private AlertDialog dialog_delete;
    private EditText et_del_username;
    private EditText et_del_password;

    private AlertDialog dialog_update;
    private EditText et_mod_username;
    private EditText et_selection;
    private EditText et_content;

    private AlertDialog dialog_query;
    private EditText et_qry_selection;
    private EditText et_qry_content;
    private String usernameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        btn_insert = findViewById(R.id.btn_insert);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_query = findViewById(R.id.btn_query);

        rv_user = findViewById(R.id.rv_user);
        mLayoutManager = new LinearLayoutManager(this);
        rv_user.setLayoutManager(mLayoutManager);
        mList.addAll(userDao.getAllData());
        mAdapter = new UserAdapter(this, mList);
        rv_user.setAdapter(mAdapter);

        btn_insert.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                showInsertDialog();
                break;
            case R.id.btn_delete:
                showDeleteDialog();
                break;
            case R.id.btn_update:
                showUpdateDialog();
                break;
            case R.id.btn_query:
                showQueryDialog();
                break;
        }
    }

    private void showInsertDialog() {
        View dialogInsert = LayoutInflater.from(this).inflate(R.layout.dialog_insert, null);
        et_add_username = dialogInsert.findViewById(R.id.et_add_username);
        et_add_password = dialogInsert.findViewById(R.id.et_add_password);
        dialog_insert = new AlertDialog.Builder(this)
                .setView(dialogInsert)
                .setTitle("增加数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = et_add_username.getText().toString().trim();
                        String password = et_add_password.getText().toString().trim();
                        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Config.USERNAME, username);
                            contentValues.put(Config.PASSWORD, password);
                            contentValues.put(Config.EMAIL, "");
                            contentValues.put(Config.SEX, "");
                            contentValues.put(Config.HOBBY, "");
                            contentValues.put(Config.BIRTHDAY, "");
                            if (userDao.insertData(contentValues)) {
                                ShowToast.showShortToast(MainActivity.this, "添加成功！");
                                refreshUserList();
                            } else {
                                ShowToast.showShortToast(MainActivity.this, "添加失败！");
                            }
                        } else {
                            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog_insert.show();
    }

    private void showDeleteDialog() {
        View dialogDelete = LayoutInflater.from(this).inflate(R.layout.dialog_delete, null);
        et_del_username = dialogDelete.findViewById(R.id.et_del_username);
        et_del_password = dialogDelete.findViewById(R.id.et_del_password);
        dialog_delete = new AlertDialog.Builder(this)
                .setView(dialogDelete)
                .setTitle("删除数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = et_del_username.getText().toString().trim();
                        String password = et_del_password.getText().toString().trim();
                        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                            if (userDao.getSomeData(Config.USERNAME, username) != null) {
                                if (password.equals(userDao.getSomeData(Config.USERNAME, username).get(0).password)) {
                                    if (userDao.deleteData(username)) {
                                        ShowToast.showShortToast(MainActivity.this, "删除成功！");
                                        refreshUserList();
                                    } else {
                                        ShowToast.showShortToast(MainActivity.this, "删除失败！");
                                    }
                                } else {
                                    ShowToast.showShortToast(MainActivity.this, "密码错误！");
                                }
                            } else {
                                ShowToast.showShortToast(MainActivity.this, "当前账号不存在！");
                            }
                        } else {
                            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog_delete.show();
    }

    private void showUpdateDialog() {
        View dialogUpdate = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        et_mod_username = dialogUpdate.findViewById(R.id.et_mod_username);
        et_selection = dialogUpdate.findViewById(R.id.et_selection);
        et_content = dialogUpdate.findViewById(R.id.et_content);
        dialog_update = new AlertDialog.Builder(this)
                .setView(dialogUpdate)
                .setTitle("修改数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mod_username = et_mod_username.getText().toString().trim();
                        String selection = et_selection.getText().toString().trim();
                        String content = et_content.getText().toString().trim();
                        if (!TextUtils.isEmpty(mod_username) && !TextUtils.isEmpty(selection) && !TextUtils.isEmpty(content)) {
                            if (userDao.updateData(mod_username, selection, content)) {
                                ShowToast.showShortToast(MainActivity.this, "修改成功！");
                                refreshUserList();
                            } else {
                                ShowToast.showShortToast(MainActivity.this, "修改失败！");
                            }
                        } else {
                            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog_update.show();
    }

    private void showQueryDialog() {
        View dialogQuery = LayoutInflater.from(this).inflate(R.layout.dialog_query, null);
        et_qry_selection = dialogQuery.findViewById(R.id.et_qry_selection);
        et_qry_content = dialogQuery.findViewById(R.id.et_qry_content);
        dialog_query = new AlertDialog.Builder(this)
                .setView(dialogQuery)
                .setTitle("查询数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String qry_selection = et_qry_selection.getText().toString().trim();
                        String qry_content = et_qry_content.getText().toString().trim();
                        if (!TextUtils.isEmpty(qry_selection) && !TextUtils.isEmpty(qry_content)) {
                            switch (qry_selection) {
                                case "邮箱":
                                    qry_selection = Config.EMAIL;
                                    break;
                                case "性别":
                                    qry_selection = Config.SEX;
                                    break;
                                case "爱好":
                                    qry_selection = Config.HOBBY;
                                    break;
                                case "生日":
                                    qry_selection = Config.BIRTHDAY;
                                    break;
                            }
                            for (int j = 0; j < userDao.getSomeData(qry_selection, qry_content).size(); j++) {
                                usernameList = userDao.getSomeData(qry_selection, qry_content).get(j).username.trim() + " ";
                            }
                            switch (qry_selection) {
                                case Config.EMAIL:
                                    qry_selection = "邮箱";
                                    break;
                                case Config.SEX:
                                    qry_selection = "性别";
                                    break;
                                case Config.HOBBY:
                                    qry_selection = "爱好";
                                    break;
                                case Config.BIRTHDAY:
                                    qry_selection = "生日";
                                    break;
                            }
                            ShowToast.showLongToast(MainActivity.this,
                                    qry_selection + "为" + qry_content + "的用户有：" + usernameList);
                        } else {
                            ShowToast.showShortToast(MainActivity.this, "输入框不能为空！");
                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog_query.show();
    }

    private void refreshUserList() {
        mList.clear();
        mList.addAll(userDao.getAllData());
        mAdapter.notifyDataSetChanged();
    }
}
