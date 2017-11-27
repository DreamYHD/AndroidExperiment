package com.example.guohouxiao.accountmanagedemo.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.guohouxiao.accountmanagedemo.bean.User;
import com.example.guohouxiao.accountmanagedemo.database.UserDBHelper;
import com.example.guohouxiao.accountmanagedemo.database.UserDao;
import com.example.guohouxiao.accountmanagedemo.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */

public class MyProvider extends ContentProvider {
    UserDao userDao;
    UserDBHelper userDBHelper;
    private static final String TAG = "MyProvider";

    @Override
    public boolean onCreate() {
        Log.d(TAG, "========= onCreate is called =========");
        userDao = new UserDao(getContext());
        userDBHelper = new UserDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "========= query is called selection =========> " + selection);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        db = userDBHelper.getReadableDatabase();
        cursor = db.query(Config.TABLE_NAME, Config.USER_COLUMNS, null, null, null, null, null);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "========= insert is called values =========> " + values);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "========= delete is called selection =========> " + selection);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "========= update is called selection =========> " + selection);
        SQLiteDatabase db = null;
        db = userDBHelper.getWritableDatabase();
        db.beginTransaction();
        assert values != null;
        db.update(Config.TABLE_NAME,
                values,
                "Username = ?",
                new String[]{String.valueOf(selection)});

        db.setTransactionSuccessful();

        db.endTransaction();
        db.close();
        return 0;
    }


}
