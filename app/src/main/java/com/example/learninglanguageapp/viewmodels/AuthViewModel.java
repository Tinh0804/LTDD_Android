package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.example.learninglanguageapp.models.Response.Result;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.repository.AuthRepository;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final SharedPrefsHelper prefs;

    private final MutableLiveData<Result<LoginResponse>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> logoutResult = new MutableLiveData<>();

    private final MutableLiveData<Result<UserResponse>> userProfileResult = new MutableLiveData<>();
    public LiveData<Result<String>> getLogoutResult() { return logoutResult; }
    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        prefs = new SharedPrefsHelper(application);
    }

    public LiveData<Result<LoginResponse>> getLoginResult() { return loginResult; }
    public LiveData<Result<UserResponse>> getUserProfileLiveData() { return userProfileResult; }
    private final MutableLiveData<Result<LoginResponse>> registerResult = new MutableLiveData<>();
    public LiveData<Result<LoginResponse>> getRegisterResult() { return registerResult; }
    public void register(RegisterRequest request) {
        repository.register(request, registerResult);
    }
    public void login(LoginRequest request) {
        repository.login(request, loginResult);
    }
    public void socialLogin(SocialLoginRequest request) {
        repository.socialLogin(request, loginResult);
    }
    public void fetchUserProfile() {
        userProfileResult.postValue(Result.loading());
        repository.fetchUserProfile(userProfileResult);
    }
    public boolean isLoggedIn() {
        return prefs.isLoggedIn();
    }
    public void logout() {
        logoutResult.postValue(Result.loading());
        repository.logout(logoutResult, getApplication().getApplicationContext());
    }
}
