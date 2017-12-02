package com.example.guohouxiao.mp3demo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guohouxiao.mp3demo.R;
import com.example.guohouxiao.mp3demo.bean.Music;
import com.example.guohouxiao.mp3demo.service.PlayerService;
import com.example.guohouxiao.mp3demo.utils.Config;
import com.example.guohouxiao.mp3demo.utils.MusicUtils;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";
    private ImageView circle_image,background_image,pre_image,next_image,paly_imahe;
    private TextView current_time_text,all_time_text,music_name_text;
    private Toolbar toolbar;
    private Music music;
    private SeekBar seekBar;
    private Context context = this;


    private boolean isStart = false;
    private boolean isPause = true;
    private boolean isFirst = true;

    public static final String UPDATE_ACTION = "com.example.guohouxiao.action.UPDATE_ACTION";
    public static final String MUSIC_CURRENT = "com.example.guohouxiao.action.MUSIC_CURRENT";
    public static final String MUSIC_DURATION = "com.example.guohouxiao.action.MUSIC_DURATION";
    private PlayMusicReceiver playerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener( v-> this.finish());
        toolbar.setTitle("");
        registerReceiver();
        init();
        if (savedInstanceState != null){
             isStart = savedInstanceState.getBoolean("isplay");
             isFirst = savedInstanceState.getBoolean("isfirst");
             isPause = savedInstanceState.getBoolean("ispause");
            if (isStart){
                paly_imahe.setImageResource(R.drawable.ic_pause_black_24dp);
            }
        }
        listener();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void listener() {
        Intent intent = new Intent();
         music = (Music) getIntent().getSerializableExtra("music");
        Log.i(TAG, "listener: "+music.getArtist());
         if (music != null && music.getCover()!=null&& !music.getCover().equals("")){
             Glide.with(this)
                     .load(music.getCover())
                     .into(circle_image);
             Glide.with(this)
                     .load(music.getCover())
                     .apply(RequestOptions.bitmapTransform(new BlurTransformation(100)))
                     .into(background_image);
         }else {
             Glide.with(this)
                     .load(getResources().getDrawable(R.drawable.defaule_pic))
                     .into(circle_image);
             Glide.with(this)
                     .load(getResources().getDrawable(R.drawable.defaule_pic))
                     .apply(RequestOptions.bitmapTransform(new BlurTransformation(100)))
                     .into(background_image);
         }
        pre_image.setOnClickListener(v->
            pre()
        );
        next_image.setOnClickListener(v->
            next()
        );
        all_time_text.setText(MusicUtils.formatTime(music.getDuration()));
        music_name_text.setText(music.getTitle());
        paly_imahe.setOnClickListener(v-> play(intent));
        seekBar.setMax((int) music.getDuration());

    }

    private void next() {
        Intent intent ;
        int index = 0;
        for (int i = 0;i<MainActivity.musicList.size();i++){
            if (music.getId() == MainActivity.musicList.get(i).getId()){
                index = i;
            }
        }
        music = MainActivity.musicList.get( index + 1);
        paly_imahe.setImageResource(R.drawable.ic_pause_black_24dp);
        intent = new Intent(this, PlayerService.class);
        intent.putExtra("url", music.getUrl());
        intent.putExtra("MSG", Config.PlayerMsg.PLAY_MSG);
        music_name_text.setText(music.getTitle());
        startService(intent);
        isFirst = false;
        isStart = true;
        isPause = false;
    }

    private void pre() {
        Intent intent ;
        int index = 0;
        for (int i = 0;i<MainActivity.musicList.size();i++){
            if (music.getId() == MainActivity.musicList.get(i).getId()){
                index = i;
            }
        }
        music = MainActivity.musicList.get( index - 1);
        paly_imahe.setImageResource(R.drawable.ic_pause_black_24dp);
        intent = new Intent(this, PlayerService.class);
        intent.putExtra("url", music.getUrl());
        intent.putExtra("MSG", Config.PlayerMsg.PLAY_MSG);
        startService(intent);
        music_name_text.setText(music.getTitle());
        isFirst = false;
        isStart = true;
        isPause = false;
    }
    private void play(Intent intent) {
        if (isFirst) {
            paly_imahe.setImageResource(R.drawable.ic_pause_black_24dp);
            intent = new Intent(this, PlayerService.class);
            intent.putExtra("url", music.getUrl());
            intent.putExtra("MSG", Config.PlayerMsg.PLAY_MSG);
            startService(intent);
            isFirst = false;
            isStart = true;
            isPause = false;
        } else {
            if (isStart) {
                paly_imahe.setImageResource(R.drawable.play);
                intent = new Intent(this, PlayerService.class);
                intent.putExtra("MSG", Config.PlayerMsg.PAUSE_MSG);
                startService(intent);
                isStart = false;
                isPause = true;
            } else if (isPause) {
                paly_imahe.setImageResource(R.drawable.ic_pause_black_24dp);
                intent = new Intent(this, PlayerService.class);
                intent.setAction("com.example.guohouxiao.meida.MUSIC_SERVICE");
                intent.putExtra("MSG", Config.PlayerMsg.CONTINUE_MSG);
                startService(intent);
                isStart = true;
                isPause = false;
            }
        }
    }

    private void registerReceiver() {
        playerReceiver = new PlayMusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        registerReceiver(playerReceiver, filter);
    }

    private void init() {
        circle_image = findViewById(R.id.circle_image);
        background_image = findViewById(R.id.background_image);
        pre_image = findViewById(R.id.pre_image);
        next_image = findViewById(R.id.next_image);
        paly_imahe = findViewById(R.id.start_image);
        current_time_text = findViewById(R.id.start_time_text);
        music_name_text = findViewById(R.id.music_name);
        all_time_text = findViewById(R.id.end_time_text);
        seekBar = findViewById(R.id.seekBar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("isplay",isStart);
        outState.putBoolean("isfirst",isFirst);
        outState.putBoolean("ispause",isPause);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    class PlayMusicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MUSIC_CURRENT)) {
                int currentTime = intent.getIntExtra("currentTime", -1);
                current_time_text.setText(MusicUtils.formatTime(currentTime));
                seekBar.setProgress(currentTime);
            } else if (action.equals(MUSIC_DURATION)) {
                int duration = intent.getIntExtra("duration", -1);
                seekBar.setProgress(duration);
                all_time_text.setText(MusicUtils.formatTime(duration));
            }else {

            }
        }
    }
}
