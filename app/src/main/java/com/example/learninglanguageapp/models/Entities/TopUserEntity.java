package com.example.learninglanguageapp.models.Entities;

public class TopUserEntity {

    private int userId;
    private String fullName;
    private String avatar;
    private int totalExperience;
    private int rank;
    private String nativeLanguage;
    private String flagIcon;
    private boolean isCurrentUser;

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public int getRank() {
        return rank;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getFlagIcon() {
        return flagIcon;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }
}
