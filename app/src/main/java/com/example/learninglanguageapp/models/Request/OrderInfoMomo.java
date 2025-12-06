package com.example.learninglanguageapp.models.Request;

public class OrderInfoMomo {
    private String orderId;
    private String orderInfo;
    private String fullName;
    private long amount;
    private int diamondAmount;     // NEW
    private boolean refillHeart;   // NEW

    public OrderInfoMomo(String orderId, String orderInfo, String fullName,
                          long amount, int diamondAmount, boolean refillHeart) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.fullName = fullName;
        this.amount = amount;
        this.diamondAmount = diamondAmount;
        this.refillHeart = refillHeart;
    }

    public String getOrderId() { return orderId; }
    public String getOrderInfo() { return orderInfo; }
    public String getFullName() { return fullName; }
    public long getAmount() { return amount; }
    public int getDiamondAmount() { return diamondAmount; }
    public boolean isRefillHeart() { return refillHeart; }

}