package com.example.learninglanguageapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.learninglanguageapp.firebase.FirebaseManager;
import com.example.learninglanguageapp.models.Progress;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.LocalStorageManager;
import com.example.learninglanguageapp.utils.Constants;

public class SyncService extends IntentService {
    private static final String TAG = "SyncService";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getStringExtra("action");
            switch (action) {
                case "SYNC_PROGRESS":
                    syncProgress();
                    break;
                case "SYNC_COURSES":
                    syncCourses();
                    break;
                case "SYNC_ALL":
                    syncAll();
                    break;
                case "UPLOAD_PENDING":
                    uploadPendingData();
                    break;
            }
        }
    }

    private void syncProgress() {
        LocalStorageManager localStorage = LocalStorageManager.getInstance(this);
        FirebaseManager firebase = FirebaseManager.getInstance(this);

        String userId = localStorage.getSharedPrefsHelper().getString(Constants.PREF_USER_ID, "");
        if (!userId.isEmpty()) {
            Progress localProgress = localStorage.getUserProgress(userId);
            if (localProgress != null && localProgress.isModified()) {
                firebase.saveProgress(userId, localProgress, task -> {
                    if (task.isSuccessful()) {
                        localProgress.setModified(false);
                        localStorage.saveProgress(localProgress);
                        Log.d(TAG, "Progress synced successfully");
                    } else {
                        Log.e(TAG, "Failed to sync progress", task.getException());
                    }
                });
            }
        }
    }

    private void syncCourses() {
        ApiService apiService = ApiClient.getInstance().getApiService();
        LocalStorageManager localStorage = LocalStorageManager.getInstance(this);

        String token = localStorage.getSharedPrefsHelper().getString(Constants.PREF_USER_TOKEN, "");
        if (!token.isEmpty()) {
//            apiService.getCourses("Bearer " + token, "all").enqueue(new Callback<List<Course>>() {
//                @Override
//                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        localStorage.saveCourses(response.body());
//                        Log.d(TAG, "Courses synced successfully");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Course>> call, Throwable t) {
//                    Log.e(TAG, "Failed to sync courses", t);
//                }
//            });
        }
    }

    private void syncAll() {
        syncProgress();
        syncCourses();
        uploadPendingData();
    }

    private void uploadPendingData() {
        // Upload any pending offline data to Firebase/API
        LocalStorageManager localStorage = LocalStorageManager.getInstance(this);
//        List<CacheEntity> pendingData = localStorage.getDatabase().cacheDao().getPendingUploads();
//
//        for (CacheEntity data : pendingData) {
//            // Process and upload each pending item
//            processPendingUpload(data);
//        }
    }

//    private void processPendingUpload(CacheEntity data) {
//        // Implementation for uploading specific cached data
//        Log.d(TAG, "Processing pending upload: " + data.getKey());
//    }
}