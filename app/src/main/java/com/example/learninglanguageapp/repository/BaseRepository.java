package com.example.learninglanguageapp.repository;

import android.content.Context;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.learninglanguageapp.firebase.FirebaseManager;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.LocalStorageManager;
import com.example.learninglanguageapp.utils.NetworkUtils;

import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRepository {
    protected ApiService apiService;
    protected LocalStorageManager localStorage;
    protected FirebaseManager firebase;
    protected Context context;

    public BaseRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getInstance().getApiService();
        this.localStorage = LocalStorageManager.getInstance(context);
        this.firebase = FirebaseManager.getInstance(context);
    }

    // Generic method to handle data synchronization
    protected <T> void syncData(
            Call<T> apiCall,
            OnDataLoadedListener<T> onSuccess,
            OnErrorListener onError,
            boolean forceRefresh
    ) {
        if (!forceRefresh) {
            // Try to load from local storage first
            T cachedData = loadFromLocal();
            if (cachedData != null) {
                onSuccess.onDataLoaded(cachedData);
                return;
            }
        }

        if (NetworkUtils.isNetworkAvailable(context)) {
            // Load from API
            apiCall.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        T data = response.body();
                        saveToLocal(data);  // Save to local storage
                        onSuccess.onDataLoaded(data);
                    } else {
                        onError.onError("API Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    onError.onError("Network Error: " + t.getMessage());
                }
            });
        } else {
            // No network, try local storage
            T cachedData = loadFromLocal();
            if (cachedData != null) {
                onSuccess.onDataLoaded(cachedData);
            } else {
                onError.onError("No network connection and no cached data");
            }
        }
    }

    protected abstract <T> T loadFromLocal();
    protected abstract <T> void saveToLocal(T data);

    public interface OnDataLoadedListener<T> {
        void onDataLoaded(T data);
    }

    public interface OnErrorListener {
        void onError(String error);
    }
}