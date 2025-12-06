package com.example.learninglanguageapp.models.UIModel;


public class LeaderBoardItem {
    private int rank;
    private String name;
    private int xp;
    private String avatarUrl;

    public LeaderBoardItem(int rank, String name, int xp, String avatarUrl) {
        this.rank = rank;
        this.name = name;
        this.xp = xp;
        this.avatarUrl = avatarUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}