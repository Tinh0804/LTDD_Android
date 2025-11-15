package com.example.learninglanguageapp.models.UIModel;


public class Option {
    private String title;
    private int iconResId;

    public Option(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
}
