package com.example.learninglanguageapp.models;


public class UserProfile {
    private String id;
    private String email;
    private long createdAt;
    private int level;
    private int xp;

    public UserProfile() {
        // Firestore cần constructor rỗng
    }

    public UserProfile(String id, String email, long createdAt, int level, int xp) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.level = level;
        this.xp = xp;
    }

    // Getter - Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
}
