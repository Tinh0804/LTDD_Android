package com.example.learninglanguageapp.storage;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String email;

    public UserEntity(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }

    public static UserEntity fromUser(FirebaseUser user) {
        return new UserEntity(user.getUid(), user.getEmail());
    }


    // Nếu bạn muốn dùng Firebase `User` khác thì sửa cho phù hợp
    public com.google.firebase.auth.FirebaseUser toUser() {
        // FirebaseUser không có constructor public nên bạn có thể trả null hoặc tạo wrapper riêng
        return null; // cần custom nếu muốn convert ngược
    }
}

