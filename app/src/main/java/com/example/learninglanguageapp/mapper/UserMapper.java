package com.example.learninglanguageapp.mapper;

import com.example.learninglanguageapp.models.Entities.UserEntity;
import com.example.learninglanguageapp.models.User;
import com.example.learninglanguageapp.models.Response.UserResponse;

public class UserMapper {
    public static UserEntity toEntity(UserResponse response) {
        return new UserEntity(
                response.getUserAccountId(),
                response.getFullName(),
                response.getPhoneNumber(),
                response.getDateOfBirth(),
                response.getNativeLanguageId(),
                response.getTotalExperience(),
                response.getCurrentStreak(),
                response.getLongestStreak(),
                response.getHearts(),
                response.getSubscriptionType(),
                response.getDiamond()
        );
    }

    public static User toDomain(UserEntity entity) {
        User u = new User();
        u.setUserId(entity.getUserId());
        u.setFullName(entity.getFullName());
        u.setPhoneNumber(entity.getPhoneNumber());
        u.setDateOfBirth(entity.getDateOfBirth());
        u.setNativeLanguageId(entity.getNativeLanguageId());
        u.setTotalExperience(entity.getTotalExperience());
        u.setCurrentStreak(entity.getCurrentStreak());
        u.setLongestStreak(entity.getLongestStreak());
        u.setHearts(entity.getHearts());
        u.setSubscriptionType(entity.getSubscriptionType());
        u.setDiamond(entity.getDiamond());
        return u;
    }
}
