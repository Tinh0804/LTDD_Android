package com.example.learninglanguageapp.viewmodels;

import android.app.Application;
import android.widget.Toast;

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

    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>();
    private MutableLiveData<PackagePayment> selectedPackageLiveData = new MutableLiveData<>();
    private MutableLiveData<PaymentResponse> paymentLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new ShopRepository(application);
    }

    public void setSelectedPackage(PackagePayment pkg) {
        selectedPackageLiveData.setValue(pkg);
    }

    public void createPayment(String method) {
        PackagePayment pkg = selectedPackageLiveData.getValue();
        if (pkg == null) {
            errorLiveData.setValue("No package selected");
            return;
        }

        String username = "LeHoangQuachTinh";

        PaymentRequest request = new PaymentRequest(
                method,
                pkg.getPrice(),
                pkg.getName(),
                username,
                pkg.getType().equals("diamond") ? pkg.getValue() : 0,
                pkg.getType().equals("heart")
        );

        repository.createPayment(method,request, paymentLiveData, loadingLiveData, errorLiveData);
    }

    public void verifyPayment(String transactionId) {
        repository.verifyPayment(transactionId, success -> {
            if (success)
//                loadBalance();
                errorLiveData.setValue(success ? "Payment successful" : "Payment failed");


            Toast.makeText(getApplication(), success ? "Payment successful" : "Payment failed", Toast.LENGTH_SHORT).show();
        }, loadingLiveData, errorLiveData);
    }

//    public void loadBalance() {
//        repository.getBalance(balanceLiveData, loadingLiveData, errorLiveData);
//    }

    public LiveData<Integer> getBalanceLiveData() { return balanceLiveData; }
    public LiveData<PackagePayment> getSelectedPackageLiveData() { return selectedPackageLiveData; }
    public LiveData<PaymentResponse> getPaymentLiveData() { return paymentLiveData; }
    public LiveData<Boolean> getLoadingLiveData() { return loadingLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
}
