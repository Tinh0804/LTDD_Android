package com.example.learninglanguageapp.storage;

import android.content.Context;
import android.content.Entity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.storage.DAOs.ExerciseDAO;
import com.example.learninglanguageapp.storage.DAOs.WordDao;

@Database(entities = {WordEntity.class, ExerciseEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract WordDao wordDao();
    public abstract ExerciseDAO exerciseDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                            AppDatabase.class, "learning_app.db")
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
