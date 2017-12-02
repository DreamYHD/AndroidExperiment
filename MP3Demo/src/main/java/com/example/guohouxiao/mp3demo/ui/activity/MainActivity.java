package com.example.guohouxiao.mp3demo.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.guohouxiao.mp3demo.R;
import com.example.guohouxiao.mp3demo.bean.Music;
import com.example.guohouxiao.mp3demo.ui.adapter.MusicAdapter;
import com.example.guohouxiao.mp3demo.utils.MusicUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext = this;
    public static List<Music>musicList = new LinkedList<>();
    private MusicAdapter musicAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      Intent intent = new Intent(this,PlayActivity.class);
//      startActivity(intent);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }else{

        }
        musicList = MusicUtils.findMusic(this);
        Log.i(TAG, "onCreate: "+musicList.size());
        recyclerView = findViewById(R.id.recycler_music);
        musicAdapter = new MusicAdapter(musicList,this);
        recyclerView.setAdapter(musicAdapter);
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter.setOnclickListener((view, position) -> {
            Intent intent = new Intent(mContext,PlayActivity.class);
            intent.putExtra("music",musicList.get(position));
            startActivity(intent);
        });
    }
}
