package com.example.learninglanguageapp.models.Request;


public class PaymentRequest {
    private String orderType = "other";
    private double amount;
    private String orderDescription;
    private String name;
    private int diamondAmount;     // NEW
    private boolean refillHeart;   // NEW
    private String redirectURL;


    public PaymentRequest(String method, double amount, String orderDescription, String name,
                          int diamondAmount, boolean refillHeart, String s) {
        this.amount = amount;
        this.orderDescription = orderDescription;
        this.name = name;
        this.diamondAmount = diamondAmount;
        this.refillHeart = refillHeart;
    }

    public String getOrderType() { return orderType; }
    public double getAmount() { return amount; }
    public String getOrderDescription() { return orderDescription; }
    public String getName() { return name; }
    public int getDiamondAmount() { return diamondAmount; }
    public boolean isRefillHeart() { return refillHeart; }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}