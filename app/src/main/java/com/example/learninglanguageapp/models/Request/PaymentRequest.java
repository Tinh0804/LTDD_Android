package com.example.learninglanguageapp.models.Request;


public class PaymentRequest {
    private int userId;
    private String packageId;
    private int amount;
    private double price;
    private String paymentMethod;

    public PaymentRequest() {
    }

    public PaymentRequest(int userId, String packageId, int amount, double price, String paymentMethod) {
        this.userId = userId;
        this.packageId = packageId;
        this.amount = amount;
        this.price = price;
        this.paymentMethod = paymentMethod;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}