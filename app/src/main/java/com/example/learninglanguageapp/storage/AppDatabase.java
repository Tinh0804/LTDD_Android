package com.example.learninglanguageapp.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.storage.DAOs.WordDao;

@Database(entities = {WordEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract WordDao wordDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                            AppDatabase.class, "learning_app.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
