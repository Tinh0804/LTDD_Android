package com.example.learninglanguageapp.firebase;

import android.content.Context;

import com.example.learninglanguageapp.models.Progress;
import com.example.learninglanguageapp.models.UserProfile;
import com.example.learninglanguageapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {

    private static FirestoreManager instance;
    private FirebaseFirestore firestore;
    private Context context;

    private FirestoreManager(Context context) {
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();

        // Configure Firestore settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    public static synchronized FirestoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new FirestoreManager(context);
        }
        return instance;
    }

    // User operations
    public void saveUser(UserProfile user, OnCompleteListener<Void> listener) {
        firestore.collection(Constants.COLLECTION_USERS)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(listener);
    }

    public void getUser(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener(listener);
    }

    public void updateUserProgress(String userId, Progress progress,
                                   OnCompleteListener<Void> listener) {
        Map<String, Object> progressData = new HashMap<>();
        progressData.put("currentLevel", progress.getCurrentLevel());
        progressData.put("totalXp", progress.getTotalXp());
        progressData.put("streak", progress.getStreak());
        progressData.put("lastActivity", progress.getLastActivity());
        progressData.put("completedLessons", progress.getCompletedLessons());

        firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_PROGRESS)
                .document("current")
                .set(progressData, SetOptions.merge())
                .addOnCompleteListener(listener);
    }

    // Leaderboard operations
    public void getLeaderboard(String timeframe, OnCompleteListener<QuerySnapshot> listener) {
        Query query = firestore.collection(Constants.COLLECTION_LEADERBOARD)
                .whereEqualTo("timeframe", timeframe)
                .orderBy("xp", Query.Direction.DESCENDING)
                .limit(50);

        query.get().addOnCompleteListener(listener);
    }

    public void updateLeaderboard(String userId, int xp, String timeframe) {
        Map<String, Object> leaderboardEntry = new HashMap<>();
        leaderboardEntry.put("userId", userId);
        leaderboardEntry.put("xp", xp);
        leaderboardEntry.put("timeframe", timeframe);
        leaderboardEntry.put("updatedAt", FieldValue.serverTimestamp());

        firestore.collection(Constants.COLLECTION_LEADERBOARD)
                .document(userId + "_" + timeframe)
                .set(leaderboardEntry, SetOptions.merge());
    }

    // Achievement operations
    public void unlockAchievement(String userId, String achievementId,
                                  OnCompleteListener<Void> listener) {
        Map<String, Object> achievement = new HashMap<>();
        achievement.put("achievementId", achievementId);
        achievement.put("unlockedAt", FieldValue.serverTimestamp());

        firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_ACHIEVEMENTS)
                .document(achievementId)
                .set(achievement)
                .addOnCompleteListener(listener);
    }

    // Real-time listeners
    public ListenerRegistration listenToUserProgress(String userId,
                                                     EventListener<DocumentSnapshot> listener) {
        return firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_PROGRESS)
                .document("current")
                .addSnapshotListener(listener);
    }

    public ListenerRegistration listenToLeaderboard(String timeframe,
                                                    EventListener<QuerySnapshot> listener) {
        return firestore.collection(Constants.COLLECTION_LEADERBOARD)
                .whereEqualTo("timeframe", timeframe)
                .orderBy("xp", Query.Direction.DESCENDING)
                .limit(50)
                .addSnapshotListener(listener);
    }
}