// network/AuthInterceptor.java
package com.example.learninglanguageapp.network;

import androidx.annotation.NonNull;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SharedPrefsHelper prefs;

    public AuthInterceptor(SharedPrefsHelper prefs) {
        this.prefs = prefs;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = prefs.getToken();
        Request.Builder builder = original.newBuilder();
        if (token != null)
            builder.header("Authorization", "Bearer " + token);
        return chain.proceed(builder.build());
    }
}