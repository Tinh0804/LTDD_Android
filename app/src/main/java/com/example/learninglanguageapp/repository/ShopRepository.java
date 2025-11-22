package com.example.learninglanguageapp.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Request.PaymentRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.BalanceResponse;
import com.example.learninglanguageapp.models.Response.PaymentResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopRepository {

    private ApiService api;
    private Context context;

    public ShopRepository(Context context) {
        this.context = context;
        this.api = ApiClient.getApiService(context);
    }

    /**
     * Lấy số dư Diamond của user
     */
    public void getBalance(int userId,
                           MutableLiveData<Integer> balanceLiveData,
                           MutableLiveData<Boolean> loadingLiveData,
                           MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

//        api.getBalance(userId).enqueue(new Callback<ApiResponse<BalanceResponse>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<BalanceResponse>> call,
//                                   Response<ApiResponse<BalanceResponse>> response) {
//
//                loadingLiveData.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    BalanceResponse data = response.body().getData();
//                    if (data != null) {
//                        balanceLiveData.setValue(data.getDiamonds());
//                    } else {
//                        errorLiveData.setValue("No balance data");
//                    }
//                } else {
//                    errorLiveData.setValue("API Error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<BalanceResponse>> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Network Error: " + t.getMessage());
//                Log.e(TAG, "getBalance failed", t);
//            }
//        });
    }

    /**
     * Tạo thanh toán và nhận payment URL
     */
    public void createPayment(int userId,
                              String packageId,
                              int amount,
                              double price,
                              String paymentMethod,
                              MutableLiveData<PaymentResponse> paymentLiveData,
                              MutableLiveData<Boolean> loadingLiveData,
                              MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

//        PaymentRequest request = new PaymentRequest(userId, packageId, amount, price, paymentMethod);

//        api.createPayment(request).enqueue(new Callback<ApiResponse<PaymentResponse>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<PaymentResponse>> call,
//                                   Response<ApiResponse<PaymentResponse>> response) {
//
//                loadingLiveData.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    PaymentResponse data = response.body().getData();
//
//                    if (data != null && data.isSuccess()) {
//                        paymentLiveData.setValue(data);
//                    } else {
//                        String message = data != null ? data.getMessage() : "Payment creation failed";
//                        errorLiveData.setValue(message);
//                    }
//                } else {
//                    errorLiveData.setValue("API Error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<PaymentResponse>> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Network Error: " + t.getMessage());
//                Log.e(TAG, "createPayment failed", t);
//            }
//        });
    }

    /**
     * Verify payment status sau khi callback từ WebView
     */
    public void verifyPayment(String transactionId,
                              MutableLiveData<Boolean> successLiveData,
                              MutableLiveData<Boolean> loadingLiveData,
                              MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

//        api.verifyPayment(transactionId).enqueue(new Callback<ApiResponse<PaymentResponse>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<PaymentResponse>> call,
//                                   Response<ApiResponse<PaymentResponse>> response) {
//
//                loadingLiveData.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    PaymentResponse data = response.body().getData();
//
//                    if (data != null) {
//                        successLiveData.setValue(data.isSuccess());
//                    } else {
//                        errorLiveData.setValue("No verification data");
//                    }
//                } else {
//                    errorLiveData.setValue("Verification failed: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<PaymentResponse>> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Network Error: " + t.getMessage());
//                Log.e(TAG, "verifyPayment failed", t);
//            }
//        });
    }
}