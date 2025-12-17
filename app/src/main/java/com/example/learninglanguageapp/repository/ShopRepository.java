package com.example.learninglanguageapp.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Request.PaymentRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.PaymentResponse;
import com.example.learninglanguageapp.models.UIModel.PackagePayment;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.utils.HelperFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopRepository {

    private ApiService apiService;
//    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public ShopRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public void createPayment(String method,PaymentRequest request,
                              MutableLiveData<PaymentResponse> liveData,
                              MutableLiveData<Boolean> loading,
                              MutableLiveData<String> error) {
        loading.postValue(true);

        if(method.equalsIgnoreCase("momo")) {
            apiService.createMomo(request).enqueue(new Callback<PaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                    loading.postValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(response.body());
                    } else {
                        error.postValue("Failed to create payment");
                    }
                }

                @Override
                public void onFailure(Call<PaymentResponse> call, Throwable t) {
                    loading.postValue(false);
                    error.postValue(t.getMessage());
                }
            });
        }
        else{
            apiService.createVnPay(request).enqueue(new Callback<PaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                    loading.postValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(response.body());
                    } else {
                        error.postValue("Failed to create payment");
                    }
                }

                @Override
                public void onFailure(Call<PaymentResponse> call, Throwable t) {
                    loading.postValue(false);
                    error.postValue(t.getMessage());
                }
            });
        }

    }

    public void verifyPayment(String transactionId, PaymentVerifyCallback callback,
                              MutableLiveData<Boolean> loading, MutableLiveData<String> error) {
        loading.postValue(true);
        apiService.verifyPayment(transactionId).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                loading.postValue(false);
                callback.onVerify(response.body() != null && response.body().isStatus());
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }

    public void getBalance(MutableLiveData<Integer> balanceLiveData,
                           MutableLiveData<Boolean> loading, MutableLiveData<String> error) {
        loading.postValue(true);
//        apiService.getBalance().enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                loading.postValue(false);
//                if (response.isSuccessful() && response.body() != null) {
//                    balanceLiveData.postValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Integer> call, Throwable t) {
//                loading.postValue(false);
//                error.postValue(t.getMessage());
//            }
//        });
    }
    public interface PurchaseCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public void purchaseWithDiamond(PackagePayment pkg, PurchaseCallback callback) {
        apiService.purchaseWithDiamond(pkg).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Giao dịch thất bại");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface PaymentVerifyCallback {
        void onVerify(boolean success);
    }
}
