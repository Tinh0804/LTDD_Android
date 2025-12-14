// models/Response/UserResponse.java
package com.example.learninglanguageapp.models.Response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("nativeLanguageId")
    private int nativeLanguageId;

    @SerializedName("totalExperience")
    private int totalExperience;

    @SerializedName("currentStreak")
    private int currentStreak;

    @SerializedName("longestStreak")
    private int longestStreak;

    @SerializedName("hearts")
    private int hearts;

    @SerializedName("subscriptionType")
    private String subscriptionType;

    @SerializedName("diamond")
    private int diamond;

    @SerializedName("userAccountId")
    private String userAccountId;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    // Getter & Setter
    public int getDiamond() { return diamond; }
    public void setDiamond(int diamond) { this.diamond = diamond; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getNativeLanguageId() { return nativeLanguageId; }
    public void setNativeLanguageId(int nativeLanguageId) { this.nativeLanguageId = nativeLanguageId; }

    public int getTotalExperience() { return totalExperience; }
    public void setTotalExperience(int totalExperience) { this.totalExperience = totalExperience; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }

    public int getHearts() { return hearts; }
    public void setHearts(int hearts) { this.hearts = hearts; }

    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }

    public String getUserAccountId() { return userAccountId; }
    public void setUserAccountId(String userAccountId) { this.userAccountId = userAccountId; }

    // Getter & Setter cho avatar
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}