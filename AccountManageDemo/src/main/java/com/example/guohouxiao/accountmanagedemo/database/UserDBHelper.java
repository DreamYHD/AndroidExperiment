package com.example.guohouxiao.accountmanagedemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.guohouxiao.accountmanagedemo.utils.Config;

/**
 * Created by guohouxiao on 2017/11/16.
 * 创建数据库和表
 */

public class UserDBHelper extends SQLiteOpenHelper {

    public UserDBHelper(Context context) {
        super(context, Config.DB_NAME, null, Config.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + Config.TABLE_NAME
                + " (Id integer primary key autoincrement,"
                + " Username text,"
                + " Password text,"
                + " Email text,"
                + " Sex text,"
                + " Hobby text,"
                + " Birthday text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + Config.TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
