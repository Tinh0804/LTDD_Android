package com.example.learninglanguageapp.models.UIModel;

public class StudyTimeOption {
    private String title;
    private String subtitle;
    private int iconResId;

    public StudyTimeOption(String title, String subtitle, int iconResId) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResId = iconResId;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public int getIconResId() { return iconResId; }
}
