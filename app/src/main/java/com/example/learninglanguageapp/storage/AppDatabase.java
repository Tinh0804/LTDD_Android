package com.example.learninglanguageapp.storage;

import android.content.Context;
import android.content.Entity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Entities.UserEntity;
import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.storage.DAOs.ExerciseDAO;
import com.example.learninglanguageapp.storage.DAOs.UserDAO;
import com.example.learninglanguageapp.storage.DAOs.WordDao;

@Database(entities = {WordEntity.class, ExerciseEntity.class, UserEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract WordDao wordDao();
    public abstract ExerciseDAO exerciseDao();
    public  abstract UserDAO userDAO();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `users` ("
                    + "`userId` TEXT NOT NULL,"
                    + "`fullName` TEXT,"
                    + "`phoneNumber` TEXT,"
                    + "`dateOfBirth` TEXT,"
                    + "`nativeLanguageId` INTEGER NOT NULL,"
                    + "`totalExperience` INTEGER NOT NULL,"
                    + "`currentStreak` INTEGER NOT NULL,"
                    + "`longestStreak` INTEGER NOT NULL,"
                    + "`hearts` INTEGER NOT NULL,"
                    + "`subscriptionType` TEXT,"
                    + "`diamond` INTEGER NOT NULL,"
                    + "PRIMARY KEY(`userId`))");
        }
    };
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "learning_app.db").addMigrations(MIGRATION_2_3).build();
        return instance;
    }
}
