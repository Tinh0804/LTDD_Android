package com.example.learninglanguageapp.utils;

public class Constants {
    // API Configuration
    public static final String BASE_URL = "";
    public static final String API_VERSION = "v1";
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;

    // Firebase Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_COURSES = "courses";
    public static final String COLLECTION_PROGRESS = "progress";
    public static final String COLLECTION_LEADERBOARD = "leaderboard";
    public static final String COLLECTION_ACHIEVEMENTS = "achievements";

    // Firebase Storage Paths
    public static final String STORAGE_PROFILE_IMAGES = "profile_images";
    public static final String STORAGE_COURSE_IMAGES = "course_images";
    public static final String STORAGE_AUDIO_FILES = "audio_files";

    // SharedPreferences Keys
    public static final String PREFS_NAME = "LanguageAppPrefs";
    public static final String PREF_USER_TOKEN = "user_token";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_LANGUAGE = "selected_language";
    public static final String PREF_DAILY_GOAL = "daily_goal";
    public static final String PREF_SOUND_ENABLED = "sound_enabled";
    public static final String PREF_NOTIFICATIONS_ENABLED = "notifications_enabled";
    public static final String PREF_OFFLINE_MODE = "offline_mode";
    public static final String PREF_LAST_SYNC = "last_sync";
    public static final String PREF_THEME_MODE = "theme_mode";

    // Database Configuration
    public static final String DATABASE_NAME = "language_app_db";
    public static final int DATABASE_VERSION = 1;

    // Cache Configuration
    public static final String CACHE_DIR_NAME = "language_app_cache";
    public static final String IMAGE_CACHE_DIR = "images";
    public static final String AUDIO_CACHE_DIR = "audio";
    public static final long CACHE_SIZE_LIMIT = 100 * 1024 * 1024; // 100MB
    public static final long CACHE_EXPIRY_TIME = 7 * 24 * 60 * 60 * 1000; // 7 days

    // File Storage
    public static final String APP_FOLDER = "LanguageApp";
    public static final String DOWNLOAD_FOLDER = "Downloads";
    public static final String BACKUP_FOLDER = "Backup";

    // Network Configuration
    public static final int RETRY_COUNT = 3;
    public static final long RETRY_DELAY = 2000; // 2 seconds

    // Sync Configuration
    public static final long SYNC_INTERVAL = 6 * 60 * 60 * 1000; // 6 hours
    public static final String SYNC_WORK_NAME = "data_sync_work";

    // Request Codes
    public static final int REQUEST_MICROPHONE_PERMISSION = 100;
    public static final int REQUEST_STORAGE_PERMISSION = 101;
    public static final int REQUEST_CAMERA_PERMISSION = 102;

    // Languages Support
    public static final String[] SUPPORTED_LANGUAGES = {
            "English", "Spanish", "French", "German", "Italian",
            "Portuguese", "Japanese", "Korean", "Chinese", "Russian"
    };

    // Game Mechanics
    public static final int XP_PER_LESSON = 10;
    public static final int XP_PERFECT_LESSON = 15;
    public static final int XP_DAILY_GOAL = 50;
    public static final int XP_STREAK_BONUS = 5;
    public static final int XP_PER_LEVEL = 100;
    public static final int MAX_LEVEL = 100;
    public static final int HEARTS_MAX = 5;
    public static final long HEART_REFILL_TIME = 30 * 60 * 1000; // 30 minutes

    // Audio Configuration
    public static final int AUDIO_SAMPLE_RATE = 44100;
    public static final int AUDIO_CHANNEL_COUNT = 1;
    public static final int AUDIO_BIT_RATE = 64000;

    // UI Configuration
    public static final long SPLASH_DELAY = 2000;
    public static final long ANIMATION_DURATION = 300;
    public static final long TOAST_DURATION = 2000;

    // Notification Configuration
    public static final String NOTIFICATION_CHANNEL_ID = "language_learning_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Language Learning";
    public static final String NOTIFICATION_CHANNEL_DESC = "Notifications for language learning app";
    public static final int DAILY_REMINDER_ID = 1001;
    public static final int STREAK_REMINDER_ID = 1002;
    public static final int SYNC_NOTIFICATION_ID = 1003;

    // Analytics Events
    public static final String EVENT_LESSON_STARTED = "lesson_started";
    public static final String EVENT_LESSON_COMPLETED = "lesson_completed";
    public static final String EVENT_QUIZ_COMPLETED = "quiz_completed";
    public static final String EVENT_LEVEL_UP = "level_up";
    public static final String EVENT_STREAK_ACHIEVED = "streak_achieved";
    public static final String EVENT_PURCHASE_MADE = "purchase_made";

    // Error Codes
    public static final int ERROR_NETWORK = 1001;
    public static final int ERROR_AUTH = 1002;
    public static final int ERROR_PERMISSION = 1003;
    public static final int ERROR_STORAGE = 1004;
    public static final int ERROR_FIREBASE = 1005;
}
