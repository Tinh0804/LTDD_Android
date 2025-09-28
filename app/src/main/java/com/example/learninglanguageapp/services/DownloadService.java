package com.example.learninglanguageapp.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.learninglanguageapp.storage.LocalStorageManager;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.NotificationUtils;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    private static final int NOTIFICATION_ID = 2001;

    private PowerManager.WakeLock wakeLock;
    private boolean isDownloading = false;

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LanguageApp::DownloadWakeLock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra("action");
            switch (action) {
                case "DOWNLOAD_COURSE":
                    String courseId = intent.getStringExtra("course_id");
//                    downloadCourse(courseId);
                    break;
                case "DOWNLOAD_AUDIO":
                    String audioUrl = intent.getStringExtra("audio_url");
                    downloadAudio(audioUrl);
                    break;
            }
        }
        return START_STICKY;
    }

    private void downloadAudio(String audioUrl) {
        if (isDownloading) return;

        isDownloading = true;
        wakeLock.acquire(10 * 60 * 1000L); // 10 phút

        startForeground(NOTIFICATION_ID, createNotification("Đang tải audio..."));

        new Thread(() -> {
            try {
                // Tạo request tải file
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(audioUrl)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // Tạo file đích
                    File audioDir = new File(getFilesDir(), "audio");
                    if (!audioDir.exists()) audioDir.mkdirs();

                    String fileName = audioUrl.substring(audioUrl.lastIndexOf("/") + 1);
                    File outputFile = new File(audioDir, fileName);

                    // Ghi dữ liệu xuống file
                    try (InputStream in = response.body().byteStream();
                         FileOutputStream out = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                    }

                    Log.d(TAG, "Audio saved to: " + outputFile.getAbsolutePath());
                    // Có thể gọi AudioCacheManager để cache lại đường dẫn
                } else {
                    Log.e(TAG, "Download audio failed: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error downloading audio", e);
            } finally {
                completeDownload();
            }
        }).start();
    }


    private void completeDownload() {
        isDownloading = false;
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        stopForeground(true);
        stopSelf();
    }

    private Notification createNotification(String content) {
        return NotificationUtils.createProgressNotification(getApplicationContext(), "Tải xuống", content);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
