package com.example.learninglanguageapp.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.*;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.AppDatabase;
import com.example.learninglanguageapp.storage.DAOs.UserDAO;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;
    private final SharedPrefsHelper prefs;

    public AuthRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
        this.prefs = new SharedPrefsHelper(context);
    }

    // login thường
    public void login(LoginRequest request, MutableLiveData<Result<LoginResponse>> liveData) {
        apiService.login(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    LoginResponse loginResp = response.body().getData();
                    prefs.saveToken(loginResp.getToken(), loginResp.getRefreshToken(), loginResp.isNewUser());
                    liveData.postValue(Result.success(loginResp));
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Đăng nhập thất bại";
                    liveData.postValue(Result.failure(new Exception(msg)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                liveData.postValue(Result.failure(new Exception("Lỗi mạng: " + t.getMessage())));
            }
        });
    }

    public void socialLogin(SocialLoginRequest request, MutableLiveData<Result<LoginResponse>> liveData) {

        apiService.socialLogin(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    LoginResponse loginResp = response.body().getData();
                    prefs.saveToken(loginResp.getToken(), loginResp.getRefreshToken(), loginResp.isNewUser());
                    // đưa dữ liệu ra ViewModel
                    liveData.postValue(Result.success(loginResp));
                } else
                    liveData.postValue(Result.failure(new Exception("Đăng nhập Google thất bại")));
            }
            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                liveData.postValue(Result.failure(new Exception("Lỗi: " + t.getMessage())));
            }
        });
    }
    // REGISTER – backend trả ApiResponse<LoginResponse>
    public void register(RegisterRequest request, MutableLiveData<Result<LoginResponse>> liveData) {
        apiService.register(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    LoginResponse loginResp = response.body().getData();

                    // Lưu token sau khi register thành công (tương tự login)
                    prefs.saveToken(
                            loginResp.getToken(),
                            loginResp.getRefreshToken(),
                            loginResp.isNewUser()
                    );

                    liveData.postValue(Result.success(loginResp));
                } else {
                    String msg = response.body() != null ?
                            response.body().getMessage() :
                            "Đăng ký thất bại";
                    liveData.postValue(Result.failure(new Exception(msg)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                liveData.postValue(Result.failure(new Exception("Lỗi mạng: " + t.getMessage())));
            }
        });
    }

    public void fetchUserProfile(MutableLiveData<Result<UserResponse>> liveData) {
        String token = prefs.getToken();
        if (token == null) {
            liveData.postValue(Result.failure(new Exception("Không có token")));
            return;
        }
        apiService.getMyProfile("Bearer " + token)
                .enqueue(new Callback<ApiResponse<UserResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                            UserResponse profile = response.body().getData();
                            prefs.saveUserFromResponse(profile);
                            liveData.postValue(Result.success(profile));
                        } else
                            liveData.postValue(Result.failure(new Exception("Không lấy được thông tin cá nhân")));
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                        liveData.postValue(Result.failure(new Exception("Lỗi: " + t.getMessage())));
                    }
                });
    }

    public void logout(MutableLiveData<Result<String>> liveData, Context context) {
        final Context appContext = context.getApplicationContext();
        new Thread(() -> {
            String refreshToken = prefs.getString(SharedPrefsHelper.KEY_REFRESH_TOKEN, null);
            try {
                UserDAO userDao = AppDatabase.getInstance(appContext).userDAO();
                userDao.deleteUser();
                prefs.logout();
            } catch (Exception e) {
                Log.e("AUTH_LOGOUT", "Failed to clear local storage: " + e.getMessage());
            }

            if (refreshToken == null) {
                liveData.postValue(Result.success("Đăng xuất cục bộ thành công"));
                return;
            }

            apiService.revokeToken(refreshToken).enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    liveData.postValue(Result.success("Đăng xuất thành công"));
                }
                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    liveData.postValue(Result.success("Đăng xuất thành công (Lỗi mạng)"));
                }
            });
        }).start();
    }


}
