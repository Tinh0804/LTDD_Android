package com.example.learninglanguageapp.models;

import java.io.Serializable;

public class User  implements Serializable {

    private String userId;
    private String phoneNumber;
    private String fullName;
    private String dateOfBirth;          // dùng String vì trong POJO thuần không cần LocalDate
    private int nativeLanguageId;
    private int totalExperience;
    private int currentStreak;
    private int longestStreak;
    private String avatar;
    private int hearts;
    private String subscriptionType;     // "free" hoặc "premium"
    private int diamond;
    // Constructor rỗng
    public User() {
        this.totalExperience = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.hearts = 5;
        this.subscriptionType = "free";
    }

    // Getter & Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getNativeLanguageId() {
        return nativeLanguageId;
    }

    public void setNativeLanguageId(int nativeLanguageId) {
        this.nativeLanguageId = nativeLanguageId;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    public int getDiamond() {
        return currentStreak;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }
    
}
