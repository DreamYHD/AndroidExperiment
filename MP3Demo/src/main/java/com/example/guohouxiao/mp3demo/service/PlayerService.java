package com.example.guohouxiao.mp3demo.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.guohouxiao.mp3demo.utils.Config;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/2.
 */

public class PlayerService extends Service {

    private static final String TAG = "PlayerService";
    private MediaPlayer mediaPlayer;
    private String path;
    private boolean isPause;
    private int currentTime;//当前播放进度
    private int duration;//时长

    public static final String UPDATE_ACTION = "com.example.guohouxiao.action.UPDATE_ACTION";
    public static final String CTL_ACTION = "com.example.guohouxiao.action.CTL_ACTION";
    public static final String MUSIC_CURRENT = "com.example.guohouxiao.action.MUSIC_CURRENT";
    public static final String MUSIC_DURATION = "com.example.guohouxiao.action.MUSIC_DURATION";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    currentTime = mediaPlayer.getCurrentPosition();
                    Intent intent = new Intent();
                    intent.setAction(MUSIC_CURRENT);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (mediaPlayer.isPlaying()) {
            stop();
        }
        path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG", 0);
        if (msg == Config.PlayerMsg.PLAY_MSG) {
            play(0);
        } else if (msg == Config.PlayerMsg.PAUSE_MSG) {
            pause();
        } else if (msg == Config.PlayerMsg.STOP_MSG) {
            stop();
        } else if (msg == Config.PlayerMsg.CONTINUE_MSG) {
            resume();
        }
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG", 0);
        if (msg == Config.PlayerMsg.PLAY_MSG) {
            play(0);
        } else if (msg == Config.PlayerMsg.PAUSE_MSG) {
            pause();
        } else if (msg == Config.PlayerMsg.STOP_MSG) {
            stop();
        } else if (msg == Config.PlayerMsg.CONTINUE_MSG) {
            resume();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void play(int currentTime) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));
            handler.sendEmptyMessage(1);
            isPause = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        if (mediaPlayer != null && !isPause) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {

        private int currentTime;
        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            if (currentTime > 0) {
                mediaPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mediaPlayer.getDuration();
            System.out.println(TAG + duration);
            intent.putExtra("duration", duration);
            sendBroadcast(intent);
        }
    }
}