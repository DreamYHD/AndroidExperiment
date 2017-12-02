package com.example.guohouxiao.mp3demo.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.guohouxiao.mp3demo.bean.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by Administrator on 2017/12/1.
 */
public class MusicUtils {

    //查找本地音乐
    public static List<Music> findMusic(Context context) {

        List<Music> musicList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {
            Music music = new Music();
            cursor.moveToNext();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String cover = getCover(context, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if (isMusic != 0) {
                music.setId(id);
                music.setTitle(title);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setSize(size);
                music.setUrl(url);
                music.setCover(cover);
                musicList.add(music);
            }
        }

        return musicList;
    }

    //读取内置专辑封面
    public static String getCover(Context context, String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cursor = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + album_id),
                projection, null, null, null);
        String cover = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            cover = cursor.getString(0);
        }
        cursor.close();
        cursor = null;
        return cover;
    }

    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}