package com.example.learninglanguageapp.viewmodels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Request.PaymentRequest;
import com.example.learninglanguageapp.models.Response.PaymentResponse;
import com.example.learninglanguageapp.models.UIModel.PackagePayment;
import com.example.learninglanguageapp.repository.ShopRepository;

public class ShopViewModel extends AndroidViewModel {

    private ShopRepository repository;

    private MutableLiveData<Integer> balanceLiveData;
    private MutableLiveData<PackagePayment> selectedPackageLiveData;
    private MutableLiveData<PaymentResponse> paymentLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    private MutableLiveData<String> errorLiveData;

    private int userId;

    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new ShopRepository(application);

        balanceLiveData = new MutableLiveData<>();
        selectedPackageLiveData = new MutableLiveData<>();
        paymentLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void loadBalance() {
        repository.getBalance(userId, balanceLiveData, loadingLiveData, errorLiveData);
    }

    public void setSelectedPackage(PackagePayment pkg) {
        selectedPackageLiveData.setValue(pkg);
    }

    public void createPayment(String method) {
//        PackagePayment pkg = selectedPackage.getValue();
//        if (pkg == null) {
//            errorLiveData.postValue("No package selected");
//            return;
//        }
//
//        String username = "User"; // sau bạn thay bằng SharedPreferences
//
//        PaymentRequest body = new PaymentRequest(
//                pkg.getPrice(),                         // amount
//                pkg.getPackageId(),                     // orderDescription
//                username,                               // name
//                pkg.getType().equals("diamond") ? pkg.getValue() : 0, // diamondAmount
//                pkg.getType().equals("heart")           // refillHeart
//        );
//
//        repository.createPayment(method, body, paymentLiveData, loadingLiveData, errorLiveData);
    }


    public void verifyPayment(String transactionId) {
        MutableLiveData<Boolean> successLiveData = new MutableLiveData<>();

        repository.verifyPayment(
                transactionId,
                successLiveData,
                loadingLiveData,
                errorLiveData
        );

        // Sau khi verify xong, reload balance
        successLiveData.observeForever(success -> {
            if (success) {
                loadBalance();
            }
        });
    }

    // Getters
    public LiveData<Integer> getBalanceLiveData() {
        return balanceLiveData;
    }

    public LiveData<PackagePayment> getSelectedPackageLiveData() {
        return selectedPackageLiveData;
    }

    public LiveData<PaymentResponse> getPaymentLiveData() {
        return paymentLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}