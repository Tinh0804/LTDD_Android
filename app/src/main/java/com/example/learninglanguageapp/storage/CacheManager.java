package com.example.learninglanguageapp.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.learninglanguageapp.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.jakewharton.disklrucache.DiskLruCache;


public class CacheManager {
    private static final String TAG = "CacheManager";
    private Context context;
    private DiskLruCache imageCache;
    private DiskLruCache audioCache;
    private File cacheDir;

    public CacheManager(Context context) {
        this.context = context;
        initializeCache();
    }

    private void initializeCache() {
        try {
            cacheDir = new File(context.getCacheDir(), Constants.CACHE_DIR_NAME);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            File imageCacheDir = new File(cacheDir, Constants.IMAGE_CACHE_DIR);
            File audioCacheDir = new File(cacheDir, Constants.AUDIO_CACHE_DIR);

            imageCache = DiskLruCache.open(imageCacheDir, 1, 1, Constants.CACHE_SIZE_LIMIT / 2);
            audioCache = DiskLruCache.open(audioCacheDir, 1, 1, Constants.CACHE_SIZE_LIMIT / 2);

        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize cache", e);
        }
    }

    public void cacheImage(String key, Bitmap bitmap) {
        try {
            String hashedKey = hashKey(key);
            DiskLruCache.Editor editor = imageCache.edit(hashedKey);
            if (editor != null) {
                OutputStream out = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.close();
                editor.commit();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to cache image", e);
        }
    }

    public Bitmap getCachedImage(String key) {
        try {
            String hashedKey = hashKey(key);
            DiskLruCache.Snapshot snapshot = imageCache.get(hashedKey);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                return bitmap;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to get cached image", e);
        }
        return null;
    }

    public void cacheAudio(String key, byte[] audioData) {
        try {
            String hashedKey = hashKey(key);
            DiskLruCache.Editor editor = audioCache.edit(hashedKey);
            if (editor != null) {
                OutputStream out = editor.newOutputStream(0);
                out.write(audioData);
                out.close();
                editor.commit();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to cache audio", e);
        }
    }

    public byte[] getCachedAudio(String key) {
        try {
            String hashedKey = hashKey(key);
            DiskLruCache.Snapshot snapshot = audioCache.get(hashedKey);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                byte[] audioData = readFully(in);
                in.close();
                return audioData;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to get cached audio", e);
        }
        return null;
    }

    public boolean isCached(String key) {
        try {
            String hashedKey = hashKey(key);
            return imageCache.get(hashedKey) != null || audioCache.get(hashedKey) != null;
        } catch (IOException e) {
            Log.e(TAG, "Failed to check cache", e);
            return false;
        }
    }

    public void clearAll() {
        try {
            imageCache.delete();
            audioCache.delete();
            initializeCache();
        } catch (IOException e) {
            Log.e(TAG, "Failed to clear cache", e);
        }
    }

    public long getCacheSize() {
        return imageCache.size() + audioCache.size();
    }

    private String hashKey(String key) {
        return String.valueOf(key.hashCode());
    }

    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        return out.toByteArray();
    }
}