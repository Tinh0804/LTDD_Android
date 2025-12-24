package com.example.learninglanguageapp.models.Response;

import com.example.learninglanguageapp.models.Entities.TopUserEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaderboardResponse {

    @SerializedName("topUsers")
    private List<TopUserEntity> topUsers;

    @SerializedName("totalUsers")
    private int totalUsers;

    public List<TopUserEntity> getTopUsers() {
        return topUsers;
    }

    public int getTotalUsers() {
        return totalUsers;
    }
}
