package com.example.learninglanguageapp.models.UIModel;


public class PackagePayment {
    private String id;
    private int amount;
    private double price;
    private String type; // "diamond" or "heart"

    public PackagePayment() {
    }

    public PackagePayment(String id, int amount, double price, String type) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}