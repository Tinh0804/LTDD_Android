package com.example.learninglanguageapp.models.Response;


public class PaymentResponse {
    private boolean success;
    private String paymentUrl;
    private String message;
    private String transactionId;

    public PaymentResponse() {
    }

    public PaymentResponse(boolean success, String paymentUrl, String message, String transactionId) {
        this.success = success;
        this.paymentUrl = paymentUrl;
        this.message = message;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}