
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
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.viewmodels.ShopViewModel;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private ImageView btnClose;
    private TextView tvTitle;

    private String paymentUrl;
    private String selectedPaymentMethod;
    private String transactionId;
    private int diamondAmount; // Số kim cương sẽ nhận được

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
        diamondAmount = getIntent().getIntExtra("diamond_amount", 0); // Lấy số kim cương từ intent

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
                Log.d("PaymentURL", "Processing: " + url);

                // --- 1. XỬ LÝ INTENT (MOMO, ZALOPAY, BANKING APP) ---
                if (url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                            return true;
                        }
                        // Fallback nếu không có app ngân hàng/momo
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            view.loadUrl(fallbackUrl);
                            return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }

                // --- 2. BẮT DEEP LINK THÀNH CÔNG (QUAN TRỌNG ĐỂ TỰ ĐÓNG) ---
                // Link này do BE redirect về: myapp://payment_success?transactionId=...
                if (url.startsWith("myapp://payment_success")) {
                    String receivedTransactionId = uri.getQueryParameter("transactionId");

                    // Gọi hàm xử lý thành công (hàm này sẽ gọi finish() để đóng WebView)
                    handlePaymentSuccess(receivedTransactionId != null ? receivedTransactionId : transactionId);
                    return true;
                }

                // --- 3. BẮT URL THÀNH CÔNG TỪ VNPAY/MOMO (KHÔNG CẦN DEEP LINK) ---
                // VNPay: vnp_ResponseCode=00 hoặc vnp_TransactionStatus=00
                if (url.contains("vnp_ResponseCode=00") || url.contains("vnp_TransactionStatus=00")) {
                    Log.d("PaymentURL", "VNPay payment success detected!");
                    handlePaymentSuccess(transactionId);
                    return true;
                }
                
                // Momo: resultCode=0 hoặc errorCode=0
                if (url.contains("resultCode=0") || url.contains("errorCode=0")) {
                    Log.d("PaymentURL", "Momo payment success detected!");
                    handlePaymentSuccess(transactionId);
                    return true;
                }

                // --- 4. BẮT URL THẤT BẠI/HỦY ---
                if (url.contains("payment_failed") || url.contains("vnp_ResponseCode=24") || url.contains("cancel")) {
                    handlePaymentFailure();
                    return true;
                }

                // --- 5. XỬ LÝ HTTP/HTTPS BÌNH THƯỜNG ---
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false; // WebView tiếp tục tự load
                }

                // --- 6. XỬ LÝ CÁC SCHEME KHÁC (tel, mailto, v.v.) ---
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri).setPackage(/* TODO: provide the application ID. For example: */ getPackageName());
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    Log.e("WebView", "Unknown scheme: " + url);
                    return true;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                // Chỉ nên dùng trong môi trường Test/Sandbox
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
        // Verify payment với backend
        viewModel.verifyPayment(finalTransactionId);

        // Cập nhật kim cương local ngay lập tức
        if (diamondAmount > 0) {
            int currentDiamond = HelperFunction.getInstance().loadUserDiamond();
            int newDiamond = currentDiamond + diamondAmount;
            HelperFunction.getInstance().saveUserDiamond(newDiamond);
            Log.d("PaymentSuccess", "Updated diamond: " + currentDiamond + " -> " + newDiamond);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "success");
        resultIntent.putExtra("transaction_id", finalTransactionId);
        resultIntent.putExtra("diamond_amount", diamondAmount); // Truyền số kim cương về
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
        if (webView != null && webView.canGoBack()) {
            webView.goBack(); // Quay lại trang trước đó của web
        } else {
            handlePaymentCancel(); // Nếu hết trang để lùi thì mới thoát
        }
    }
}