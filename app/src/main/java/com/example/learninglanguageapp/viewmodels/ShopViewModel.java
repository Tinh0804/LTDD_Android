
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
import com.example.learninglanguageapp.utils.HelperFunction;

public class ShopViewModel extends AndroidViewModel {

    private ShopRepository repository;

    private MutableLiveData<Integer> balanceLiveData = new MutableLiveData<>();
    private MutableLiveData<PackagePayment> selectedPackageLiveData = new MutableLiveData<>();
    private MutableLiveData<PaymentResponse> paymentLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> purchaseSuccessLiveData = new MutableLiveData<>();

    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new ShopRepository(application);
        loadBalance();
    }
    public void loadBalance() {
        // Lấy số kim cương từ HelperFunction (Shared Preferences)
        int currentDiamond = HelperFunction.getInstance().loadUserDiamond();
        balanceLiveData.setValue(currentDiamond);
    }



    public void purchaseWithDiamond(PackagePayment pkg) {
        if (pkg == null) {
            errorLiveData.setValue("Gói hàng không hợp lệ");
            return;
        }

        Integer currentBalance = balanceLiveData.getValue();
        if (currentBalance == null || currentBalance < pkg.getPrice()) {
            errorLiveData.setValue("Không đủ kim cương!");
            purchaseSuccessLiveData.setValue(false);
            return;
        }

        loadingLiveData.setValue(true);

        repository.purchaseWithDiamond(
                pkg,
                new ShopRepository.PurchaseCallback() {
                    @Override
                    public void onSuccess() {
                        loadingLiveData.setValue(false);

                        // 1. Lấy số dư hiện tại và trừ đi
                        int currentBalance = HelperFunction.getInstance().loadUserDiamond();
                        int newBalance = currentBalance - (int) pkg.getPrice();

                        // 2. Lưu vào Local (SharedPrefs)
                        HelperFunction.getInstance().saveUserDiamond(newBalance);

                        // 3. Cập nhật LiveData để UI tự động đổi số
                        balanceLiveData.setValue(newBalance);

                        // 4. Thông báo thành công
                        purchaseSuccessLiveData.setValue(true);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(errorMessage);
                        purchaseSuccessLiveData.setValue(false);
                    }
                }
        );
    }

    public void setSelectedPackage(PackagePayment pkg) {
        selectedPackageLiveData.setValue(pkg);
    }

    public void createPayment(String method) {
        PackagePayment pkg = selectedPackageLiveData.getValue();
        if (pkg == null) {
            errorLiveData.setValue("Chưa chọn gói hàng");
            return;
        }

        // Lấy username từ SharedPreferences hoặc UserSession
        String username = getUsernameFromSession();

        PaymentRequest request = new PaymentRequest(
                method,
                pkg.getPrice(),
                pkg.getName(),
                username,
                pkg.getType().equals("diamond") ? pkg.getValue() : 0,
                pkg.getType().equals("heart"),
                "myapp://payment_success"
        );

        loadingLiveData.setValue(true);
        repository.createPayment(method, request, paymentLiveData, loadingLiveData, errorLiveData);
    }
    public void verifyPayment(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            errorLiveData.setValue("Mã giao dịch không hợp lệ");
            return;
        }

        loadingLiveData.setValue(true);
        repository.verifyPayment(transactionId, success -> {
            if (success)
            {
                loadBalance();
                errorLiveData.setValue(success ? "Payment successful" : "Payment failed");
            }


            Toast.makeText(getApplication(), success ? "Payment successful" : "Payment failed", Toast.LENGTH_SHORT).show();
        }, loadingLiveData, errorLiveData);
    }

    public void deductDiamonds(int amount) {
        Integer currentBalance = balanceLiveData.getValue();
        if (currentBalance == null) {
            currentBalance = 0;
        }

        int newBalance = Math.max(0, currentBalance - amount);
        HelperFunction.getInstance().saveUserDiamond(newBalance);
        balanceLiveData.setValue(newBalance);
    }

    public boolean hasEnoughDiamonds(int requiredAmount) {
        Integer currentBalance = balanceLiveData.getValue();
        return currentBalance != null && currentBalance >= requiredAmount;
    }

    private String getUsernameFromSession() {
        // TODO: Lấy username từ SharedPreferences hoặc UserSession
        // Ví dụ:
        // SharedPreferences prefs = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        // return prefs.getString("username", "");

        return "LeHoangQuachTinh";
    }

    public void clearError() {
        errorLiveData.setValue(null);
    }

    public void clearPurchaseSuccess() {
        purchaseSuccessLiveData.setValue(null);
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

    public LiveData<Boolean> getPurchaseSuccessLiveData() {
        return purchaseSuccessLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Cleanup nếu cần
        repository = null;
    }
}