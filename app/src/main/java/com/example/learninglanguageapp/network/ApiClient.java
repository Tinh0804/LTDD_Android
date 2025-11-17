package com.example.learninglanguageapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String PREF_NAME = "api_config";
    private static final String KEY_BASE_URL = "base_url";

    // Default URLs
//    private static final String DEFAULT_EMULATOR_URL = "http://10.0.2.2:5111/";
    private static final String DEFAULT_EMULATOR_URL = "http://192.168.99.121:5050/";
    private static final String DEFAULT_REAL_DEVICE_URL = "http://192.168.99.121:5050/"; // Thay IP của bạn

    private static Retrofit retrofit;
    private static ApiService apiService;
    private static String currentBaseUrl;

    /**
     * Khởi tạo với context để lấy BASE_URL từ SharedPreferences
     */
    public static void init(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        currentBaseUrl = prefs.getString(KEY_BASE_URL, DEFAULT_EMULATOR_URL);
        Log.i(TAG, "Initialized with BASE_URL: " + currentBaseUrl);
    }

    /**
     * Thay đổi BASE_URL
     */
    public static void setBaseUrl(Context context, String baseUrl) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_BASE_URL, baseUrl).apply();
            currentBaseUrl = baseUrl;
            reset();
            Log.i(TAG, "BASE_URL changed to: " + baseUrl);
        }
    }

    public static String getCurrentBaseUrl() {
        return currentBaseUrl != null ? currentBaseUrl : DEFAULT_EMULATOR_URL;
    }

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.d(TAG, message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    String baseUrl = getCurrentBaseUrl();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(createOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create(createGson()))
                            .build();

                    Log.i(TAG, "Retrofit initialized with BASE_URL: " + baseUrl);
                }
            }
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (ApiClient.class) {
                if (apiService == null) {
                    apiService = getRetrofitInstance().create(ApiService.class);
                    Log.i(TAG, "ApiService created");
                }
            }
        }
        return apiService;
    }

    public static void reset() {
        retrofit = null;
        apiService = null;
        Log.i(TAG, "ApiClient reset");
    }
}