// File: com.example.learninglanguageapp.models.Entities.UserEntity.java
package com.example.learninglanguageapp.models.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    // Primary Key
    @PrimaryKey
    @NonNull
    private String userId; // userAccountId

    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private int nativeLanguageId;
    private int totalExperience;
    private int currentStreak;
    private int longestStreak;
    private int hearts;
    private String subscriptionType;
    private int diamond;

    public UserEntity(@NonNull String userId, String fullName, String phoneNumber,
                      String dateOfBirth, int nativeLanguageId, int totalExperience,
                      int currentStreak, int longestStreak, int hearts,
                      String subscriptionType, int diamond) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.nativeLanguageId = nativeLanguageId;
        this.totalExperience = totalExperience;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.hearts = hearts;
        this.subscriptionType = subscriptionType;
        this.diamond = diamond;
    }
    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
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
    public int getDiamond() { return diamond; }
    public void setDiamond(int diamond) { this.diamond = diamond; }
}
