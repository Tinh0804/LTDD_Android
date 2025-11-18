package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.viewmodels.ShopViewModel;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";

    private ShopViewModel viewModel;

    // Views
    private WebView webView;
    private ProgressBar progressBar;
    private ImageView btnClose;
    private TextView tvTitle;

    // Data
    private String paymentUrl;
    private String paymentMethod;
    private String transactionId;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Init ViewModel
        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        initViews();
        getIntentData();
        setupWebView();
        setupObservers();

        // Load payment URL
        loadPaymentUrl();
    }

    private void initViews() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        paymentUrl = intent.getStringExtra("payment_url");
        paymentMethod = intent.getStringExtra("payment_method");
        transactionId = intent.getStringExtra("transaction_id");

        if (paymentMethod != null) {
            String title = paymentMethod.equalsIgnoreCase("vnpay") ? "VNPay Payment" : "Momo Payment";
            tvTitle.setText(title);
        }

        Log.d(TAG, "Payment URL: " + paymentUrl);
        Log.d(TAG, "Transaction ID: " + transactionId);
    }

    private void setupObservers() {
        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupWebView() {
        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "Page started: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Page finished: " + url);

                // Check payment status từ URL
                checkPaymentStatus(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "URL Loading: " + url);

                // Check for payment callback URLs
                if (isPaymentSuccessUrl(url)) {
                    handlePaymentSuccess();
                    return true;
                } else if (isPaymentFailedUrl(url)) {
                    handlePaymentFailure();
                    return true;
                }

                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        btnClose.setOnClickListener(v -> {
            // User cancel
            handlePaymentCancel();
        });
    }

    private void loadPaymentUrl() {
        if (paymentUrl != null && !paymentUrl.isEmpty()) {
            webView.loadUrl(paymentUrl);
        } else {
            Toast.makeText(this, "Invalid payment URL", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void checkPaymentStatus(String url) {
        Uri uri = Uri.parse(url);
        String status = uri.getQueryParameter("status");
        String code = uri.getQueryParameter("vnp_ResponseCode");

        // VNPay: vnp_ResponseCode = 00 là success
        if ("00".equals(code)) {
            handlePaymentSuccess();
        } else if (status != null) {
            if ("success".equalsIgnoreCase(status)) {
                handlePaymentSuccess();
            } else if ("failed".equalsIgnoreCase(status) || "cancel".equalsIgnoreCase(status)) {
                handlePaymentFailure();
            }
        }
    }

    private boolean isPaymentSuccessUrl(String url) {
        return url.contains("payment_success") ||
                url.contains("/success") ||
                url.contains("vnp_ResponseCode=00") ||
                url.contains("resultCode=0"); // Momo success
    }

    private boolean isPaymentFailedUrl(String url) {
        return url.contains("payment_failed") ||
                url.contains("payment_error") ||
                url.contains("/cancel") ||
                url.contains("/failed");
    }

    private void handlePaymentSuccess() {
        Log.d(TAG, "Payment Success - Transaction ID: " + transactionId);

        // Verify payment với backend
        if (transactionId != null && !transactionId.isEmpty()) {
            viewModel.verifyPayment(transactionId);
        }

        Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "success");
        resultIntent.putExtra("transaction_id", transactionId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void handlePaymentFailure() {
        Log.d(TAG, "Payment Failed");

        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "failed");
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    private void handlePaymentCancel() {
        Log.d(TAG, "Payment Cancelled by user");

        Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "cancelled");
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            handlePaymentCancel();
        }
    }
}