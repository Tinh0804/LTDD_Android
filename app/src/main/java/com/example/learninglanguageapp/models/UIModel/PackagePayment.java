package com.example.learninglanguageapp.models.UIModel;


import java.io.Serializable;

public class PackagePayment implements Serializable {
    private String name;
    private int value;          // Số diamond hoặc heart
    private double price;       // Giá tiền
    private String type;        // "diamond" hoặc "heart"

    public PackagePayment( String name, int value, double price, String type) {
        this.name = name;
        this.value = value;
        this.price = price;
        this.type = type;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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