
package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
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

    private WebView webView;
    private ProgressBar progressBar;
    private ImageView btnClose;
    private TextView tvTitle;

    private String paymentUrl;
    private String selectedPaymentMethod;
    private String transactionId;

    private ShopViewModel viewModel;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        // 1. Initialize Views
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar2);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);

        paymentUrl = getIntent().getStringExtra("payment_url");
        selectedPaymentMethod = getIntent().getStringExtra("payment_method");
        transactionId = getIntent().getStringExtra("transaction_id");

        if (tvTitle != null) {
            tvTitle.setText(selectedPaymentMethod != null && selectedPaymentMethod.equalsIgnoreCase("vnpay") ? "VNPay Payment" : "Momo Payment");
        }

        // 3. Setup and Load
        if (webView != null && paymentUrl != null) {
            setupWebView();
            webView.loadUrl(paymentUrl);

        } else {
            Toast.makeText(this, "Error: WebView is missing or payment URL is invalid.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> handlePaymentCancel());
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // Bắt buộc
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Cho phép popup / window.open
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

        // Giả lập Chrome Android
        settings.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 13; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0 Mobile Safari/537.36"
        );

        // Bật cookie
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        android.webkit.CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Uri uri = request.getUrl();
                Log.d("PaymentURL", "Override URL: " + url);

                if (url.startsWith("intent://")) {
                    try {
                        // Chuyển Intent URL thành Intent Android
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                        // Nếu có ứng dụng để xử lý Intent này (Momo)
                        if (view.getContext().getPackageManager().resolveActivity(intent, 0) != null) {
                            view.getContext().startActivity(intent);
                        } else {
                            // Nếu không có ứng dụng Momo, chuyển hướng đến link tải (fallback)
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            if (fallbackUrl != null) {
                                view.loadUrl(fallbackUrl);
                            } else {
                                Toast.makeText(view.getContext(), "Ứng dụng Momo chưa được cài đặt.", Toast.LENGTH_LONG).show();
                            }
                        }
                        return true; // Quan trọng: Đã xử lý URL này
                    } catch (Exception e) {
                        Log.e("MomoIntent", "Error handling Momo Intent URL: " + e.getMessage());
                        return true;
                    }
                }

                // --- 2. LOGIC BẮT DEEP LINK CALLBACK (myapp://payment_success) ---
                if (url.startsWith("myapp://payment_success")) {
                    Uri callbackUri = Uri.parse(url);
                    String receivedTransactionId = callbackUri.getQueryParameter("transactionId");

                    handlePaymentSuccess(receivedTransactionId != null ? receivedTransactionId : transactionId);
                    return true; // Đã xử lý callback
                }

                // --- 3. LOGIC BẮT URL THẤT BẠI CỦA VNPAY/MOMO ---
                else if (url.contains("payment_failed") || url.contains("/cancel") || url.contains("vnp_ResponseCode=24")) {
                    handlePaymentFailure();
                    return true;
                }

                // --- 4. Cho phép WebView xử lý URL HTTP/HTTPS thông thường ---
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false; // Trả về false để WebView tự tải
                }

                // Xử lý các scheme khác (tel:, mailto:)
                try {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri).setClassName(/* TODO: provide the application ID. For example: */ getPackageName(), "com.facebook.CustomTabActivity"));
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                // Sandbox / Test environment: bỏ qua SSL
                handler.proceed();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, android.os.Message resultMsg) {
                // Xử lý popup của VNPay
                WebView newWebView = new WebView(WebViewActivity.this);
                newWebView.getSettings().setJavaScriptEnabled(true);
                newWebView.getSettings().setDomStorageEnabled(true);
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        webView.loadUrl(url); // Load trở lại WebView chính
                    }
                });

                ((WebView.WebViewTransport) resultMsg.obj).setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar != null) {
                    progressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });

        webView.loadUrl(paymentUrl); // Load URL VNPay
    }


    private void handlePaymentSuccess(String finalTransactionId) {
        viewModel.verifyPayment(finalTransactionId);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "success");
        resultIntent.putExtra("transaction_id", finalTransactionId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void handlePaymentFailure() {
        Toast.makeText(this, "Payment process failed or was cancelled.", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "failed");
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    private void handlePaymentCancel() {
        Toast.makeText(this, "Payment process cancelled by user.", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "cancelled");
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            handlePaymentCancel();
        }
    }
}