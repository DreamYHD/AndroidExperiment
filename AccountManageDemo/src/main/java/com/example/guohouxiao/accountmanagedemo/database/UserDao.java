package com.example.guohouxiao.accountmanagedemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.guohouxiao.accountmanagedemo.bean.User;
import com.example.guohouxiao.accountmanagedemo.utils.Config;
import com.example.guohouxiao.accountmanagedemo.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohouxiao on 2017/11/16.
 * 处理数据操作方法
 */

public class UserDao {

    private static final String TAG = "UserDao";

    private Context context;
    private UserDBHelper userDBHelper;

    public UserDao(Context context) {
        this.context = context;
        userDBHelper = new UserDBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = userDBHelper.getReadableDatabase();
            cursor = db.query(Config.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * 初始化数据，建表
     */
    public void initTable() {
        SQLiteDatabase db = null;
        try {
            db = userDBHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 执行自定义SQL语句
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = null;
        try {
            if (sql.contains("select")) {
                ShowToast.showShortToast(context, "Sorry，还没处理select语句");
            } else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")) {
                db = userDBHelper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
                ShowToast.showShortToast(context, "执行SQL语句成功");
            }
        } catch (Exception e) {
            ShowToast.showShortToast(context, "执行出错，请检查SQL语句");
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    /**
     * 查询数据库中所有数据
     */
    public List<User> getAllData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = userDBHelper.getReadableDatabase();
            cursor = db.query(Config.TABLE_NAME, Config.USER_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<User> userList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    userList.add(parseUser(cursor));
                }
                return userList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 新增一条数据
     */
    public boolean insertData(ContentValues contentValues) {
        SQLiteDatabase db = null;
        try {
            db = userDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.insertOrThrow(Config.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }


    /**
     * 删除一条数据
     */
    public boolean deleteData(String username) {
        SQLiteDatabase db = null;
        try {
            db = userDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(Config.TABLE_NAME, "Username = ?", new String[]{String.valueOf(username)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 修改一条数据
     */
    public boolean updateData(String username, String selection, String content) {
        SQLiteDatabase db = null;
        try {
            db = userDBHelper.getWritableDatabase();
            db.beginTransaction();
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
            db.update(Config.TABLE_NAME,
                    cv,
                    "Username = ?",
                    new String[]{String.valueOf(username)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 数据查询
     */
    public List<User> getSomeData(String selection, String content) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = userDBHelper.getReadableDatabase();
            cursor = db.query(Config.TABLE_NAME,
                    Config.USER_COLUMNS,
                    selection + " = ?",
                    new String[]{content},
                    null, null, null);
            if (cursor.getCount() > 0) {
                List<User> userList = new ArrayList<User>(cursor.getCount());
                while (cursor.moveToNext()) {
                    User user = parseUser(cursor);
                    userList.add(user);
                }
                return userList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 将查找到的数据转换成User类
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

}
