package com.example.learninglanguageapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AudioPlaybackService extends Service implements AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "AudioPlaybackService";
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private boolean isPaused = false;

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initializeMediaPlayer();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mediaPlayer == null) initializeMediaPlayer();
                else if (isPaused) {
                    mediaPlayer.start();
                    isPaused = false;
                }
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                stopAudio();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPaused = true;
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.3f, 0.3f);
                }
                break;
        }
    }
    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // Khi phát xong thì giải phóng audio focus và stop service
            mediaPlayer.setOnCompletionListener(mp -> {
                releaseAudioFocus();
                stopSelf();
            });

            // Khi gặp lỗi
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                releaseAudioFocus();
                stopSelf();
                return true;
            });
        }
    }

    private void playAudio(String audioPath) {
        try {
            requestAudioFocus();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            releaseAudioFocus();
        }
    }

    private void requestAudioFocus() {
        audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void releaseAudioFocus() {
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        releaseAudioFocus();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // vì đây là started service, không cần bind
    }
}