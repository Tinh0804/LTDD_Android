package com.example.learninglanguageapp.models.Request;


import com.google.gson.annotations.SerializedName;

public class PaymentRequest {
    @SerializedName("userId")
    private int userId;
    private String orderType = "other";
    private double amount;
    private String orderDescription;
    private String name;
    private int diamondAmount;     // NEW
    private boolean refillHeart;   // NEW


    public PaymentRequest(double amount, String orderDescription, String name,
                               int diamondAmount, boolean refillHeart) {
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
}