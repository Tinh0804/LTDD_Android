package com.example.learninglanguageapp.storage.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.learninglanguageapp.models.Entities.UserEntity;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getCurrentUser();

    @Query("DELETE FROM users")
    void deleteUser();

    @Query("SELECT EXISTS(SELECT * FROM users LIMIT 1)")
    boolean hasUser();
}
