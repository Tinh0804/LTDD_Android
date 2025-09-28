package com.example.learninglanguageapp.storage;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.learninglanguageapp.models.Progress;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LocalStorageManager {
    private static LocalStorageManager instance;
    private Context context;
    private SharedPrefsHelper sharedPrefsHelper;
    private AppDatabase database;
    private CacheManager cacheManager;

    private LocalStorageManager(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPrefsHelper = new SharedPrefsHelper(this.context);
        this.database = AppDatabase.getInstance(this.context);
        this.cacheManager = new CacheManager(this.context);
    }

    public static synchronized LocalStorageManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocalStorageManager(context);
        }
        return instance;
    }

    // User data
    public void saveUser(FirebaseUser user) {
        database.userDao().insertUser(UserEntity.fromUser(user));
        sharedPrefsHelper.saveString(Constants.PREF_USER_ID, user.getUid());
    }

    public FirebaseUser getUser(String userId) {
        UserEntity entity = database.userDao().getUserById(userId);
        return entity != null ? entity.toUser() : null;
    }






    // Cache management
    public void cacheImage(String url, Bitmap bitmap) {
        cacheManager.cacheImage(url, bitmap);
    }

    public Bitmap getCachedImage(String url) {
        return cacheManager.getCachedImage(url);
    }

    public void cacheAudio(String url, byte[] audioData) {
        cacheManager.cacheAudio(url, audioData);
    }

    public byte[] getCachedAudio(String url) {
        return cacheManager.getCachedAudio(url);
    }

    // Clear data
    public void clearAllData() {
//        database.clearAllTables();
//        sharedPrefsHelper.clearAll();
        cacheManager.clearAll();
    }

    public void clearCache() {
        cacheManager.clearAll();
    }

    public void saveProgress(Progress localProgress) {

    }

    public Progress getUserProgress(String userId) {
        return null;
    }

    public SharedPrefsHelper getSharedPrefsHelper() {
        return sharedPrefsHelper;
    }
}
