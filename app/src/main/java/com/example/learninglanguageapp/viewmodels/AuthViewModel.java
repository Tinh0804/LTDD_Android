package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.Result;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.repository.AuthRepository;


public class AuthViewModel extends AndroidViewModel {
    private AuthRepository repository;
    private MutableLiveData<Result<UserResponse>> loginResult = new MutableLiveData<>();
    private MutableLiveData<Result<UserResponse>> registerResult = new MutableLiveData<>();
    private MutableLiveData<Result<UserResponse>> socialLoginResult = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application.getApplicationContext());
    }

    public LiveData<Result<UserResponse>> getLoginResult() { return loginResult; }
    public LiveData<Result<UserResponse>> getRegisterResult() { return registerResult; }
    public LiveData<Result<UserResponse>> getSocialLoginResult() { return socialLoginResult; }

    public void login(LoginRequest request) { repository.login(request, loginResult); }
    public void register(RegisterRequest request) { repository.register(request, registerResult); }
    public void socialLogin(SocialLoginRequest request) { repository.socialLogin(request, socialLoginResult); }
}
